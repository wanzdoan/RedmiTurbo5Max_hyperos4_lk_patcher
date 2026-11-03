// ffff000090771234  size=124  FUN_ffff000090771234

void FUN_ffff000090771234(long *param_1)

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
      thunk_FUN_ffff0000907c2ad8();
    } while (plVar3 != (long *)0x0);
    if (bVar2) {
      FUN_ffff00009076fc00
                (s_ERROR___s____ffff0000907f352e,s_ufdt_node_pool_destruct_ffff0000907d1d0c);
      FUN_ffff00009076fc00(s_Some_nodes_aren_t_freed_before_u_ffff0000907d6b2b);
    }
  }
  *param_1 = 0;
  param_1[1] = 0;
  return;
}


