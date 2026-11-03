
void FUN_ffff0000907714fc(long *param_1)

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
      thunk_FUN_ffff0000907c314c();
    } while (plVar3 != (long *)0x0);
    if (bVar2) {
      FUN_ffff00009076fec8
                (s_ERROR___s____ffff0000907f36ba,s_ufdt_node_pool_destruct_ffff0000907d1d49);
      FUN_ffff00009076fec8(s_Some_nodes_aren_t_freed_before_u_ffff0000907d6bc1);
    }
  }
  *param_1 = 0;
  param_1[1] = 0;
  return;
}


