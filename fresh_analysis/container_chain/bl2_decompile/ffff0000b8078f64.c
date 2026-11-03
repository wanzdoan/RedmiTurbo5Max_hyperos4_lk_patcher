
void FUN_ffff0000b8078f64(long *param_1,long *param_2)

{
  int iVar1;
  long *plVar2;
  long *plVar3;
  long lVar4;
  long *plVar5;
  
  plVar3 = param_1;
  do {
    plVar2 = plVar3;
    plVar3 = (long *)*plVar2;
    if (plVar3 == (long *)0x0) {
      FUN_ffff0000b80777d0(s_ERROR___s____ffff0000b809976e,s_ufdt_node_pool_free_ffff0000b8095019);
      FUN_ffff0000b80777d0(s_Given_node_is_not_in_the_pool__ffff0000b809a529);
      return;
    }
  } while ((param_2 < plVar3 + 3) || (plVar3 + 0x1003 <= param_2));
  lVar4 = plVar3[1];
  plVar3[1] = (long)param_2;
  *param_2 = lVar4;
  plVar5 = (long *)*plVar2;
  iVar1 = (int)plVar3[2] + -1;
  lVar4 = *plVar5;
  *(int *)(plVar3 + 2) = iVar1;
  *plVar2 = lVar4;
  if (lVar4 == 0) {
    param_1[1] = (long)plVar2;
  }
  lVar4 = *param_1;
  *plVar5 = 0;
  if (iVar1 == 0) {
    if (lVar4 != 0) {
      if (*(long *)(lVar4 + 8) == 0) {
        *plVar3 = lVar4;
        *param_1 = (long)plVar3;
        return;
      }
      thunk_FUN_ffff0000b8082558(plVar3);
      return;
    }
    *plVar3 = 0;
    *param_1 = (long)plVar3;
  }
  else {
    *plVar3 = lVar4;
    *param_1 = (long)plVar3;
    if (lVar4 != 0) {
      return;
    }
  }
  param_1[1] = (long)plVar3;
  return;
}


