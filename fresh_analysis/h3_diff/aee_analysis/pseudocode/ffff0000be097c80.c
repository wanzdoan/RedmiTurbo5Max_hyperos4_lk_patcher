
void FUN_ffff0000be097c80(long *param_1)

{
  long *plVar1;
  bool bVar2;
  long *plVar3;
  
  plVar3 = (long *)*param_1;
  if (plVar3 != (long *)0x0) {
    bVar2 = false;
    do {
      plVar1 = plVar3 + 2;
      plVar3 = (long *)*plVar3;
      if ((int)*plVar1 != 0) {
        bVar2 = true;
      }
      thunk_FUN_ffff0000be09937c();
    } while (plVar3 != (long *)0x0);
    if (bVar2) {
      FUN_ffff0000be09664c
                (s_ERROR___s____ffff0000be0bcb11,s_ufdt_node_pool_destruct_ffff0000be0a53b8);
      FUN_ffff0000be09664c(s_Some_nodes_aren_t_freed_before_u_ffff0000be0a8a8a);
    }
  }
  *param_1 = 0;
  param_1[1] = 0;
  return;
}


