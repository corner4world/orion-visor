import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import type { HostQueryResponse } from '@/api/asset/host';
import axios from 'axios';

/**
 * 计划执行创建请求
 */
export interface ExecJobCreateRequest {
  name?: string;
  expression?: string;
  timeout?: number;
  command?: string;
  parameterSchema?: string;
  hostIdList?: Array<number>;
}

/**
 * 计划执行更新请求
 */
export interface ExecJobUpdateRequest extends ExecJobCreateRequest {
  id?: number;
}

/**
 * 计划执行状态更新请求
 */
export interface ExecJobUpdateStatusRequest {
  id: number;
  status: number;
}

/**
 * 计划执行查询请求
 */
export interface ExecJobQueryRequest extends Pagination {
  id?: number;
  name?: string;
  command?: string;
  status?: number;
  queryRecentLog?: boolean;
}

/**
 * 计划执行查询响应
 */
export interface ExecJobQueryResponse extends TableData {
  id: number;
  name: string;
  expression: string;
  timeout: number;
  command: string;
  parameterSchema: string;
  status: number;
  recentLogId: number;
  recentLogStatus: string;
  recentLogTime: number;
  createTime: number;
  updateTime: number;
  hostIdList: Array<number>;
  hostList: Array<HostQueryResponse>;
}

/**
 * 创建计划执行
 */
export function createExecJob(request: ExecJobCreateRequest) {
  return axios.post('/asset/exec-job/create', request);
}

/**
 * 更新计划执行
 */
export function updateExecJob(request: ExecJobUpdateRequest) {
  return axios.put('/asset/exec-job/update', request);
}

/**
 * 更新计划执行状态
 */
export function updateExecJobStatus(request: ExecJobUpdateStatusRequest) {
  return axios.put('/asset/exec-job/update-status', request);
}

/**
 * 查询计划执行
 */
export function getExecJob(id: number) {
  return axios.get<ExecJobQueryResponse>('/asset/exec-job/get', { params: { id } });
}

/**
 * 分页查询计划执行
 */
export function getExecJobPage(request: ExecJobQueryRequest) {
  return axios.post<DataGrid<ExecJobQueryResponse>>('/asset/exec-job/query', request);
}

/**
 * 删除计划执行
 */
export function deleteExecJob(id: number) {
  return axios.delete('/asset/exec-job/delete', { params: { id } });
}

/**
 * 手动触发计划执行任务
 */
export function triggerExecJob(id: number) {
  return axios.post('/asset/exec-job/trigger', { id });
}
