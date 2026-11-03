
undefined8 FUN_ffff0000b8079f38(int *param_1,long param_2)

{
  long *plVar1;
  long lVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  int iVar6;
  ulong uVar7;
  undefined8 uVar8;
  ulong local_58;
  undefined8 local_50;
  long lStack_48;
  
  plVar1 = (long *)FUN_ffff0000b807a0ac();
  if (plVar1 == (long *)0x0) {
    FUN_ffff0000b80777d0(s_ERROR___s____ffff0000b809976e,s_ufdt_prop_dict_add_ffff0000b809f10d);
    FUN_ffff0000b80777d0(s_ufdt_prop_dict__failed_to_find_n_ffff0000b8098936);
LAB_ffff0000b8079f9c:
    uVar8 = 0xffffffff;
  }
  else {
    if (*plVar1 == 0) {
      iVar3 = param_1[1] + 1;
      param_1[1] = iVar3;
      *plVar1 = param_2;
    }
    else {
      *plVar1 = param_2;
      if (param_1 == (int *)0x0) goto LAB_ffff0000b8079f9c;
      iVar3 = param_1[1];
    }
    uVar7 = (long)*param_1 << 1;
    iVar6 = (int)uVar7;
    if (iVar6 < iVar3 * 3) {
      lVar4 = (long)*param_1 << 4;
      uVar8 = *(undefined8 *)(param_1 + 2);
      local_50 = 0;
      lStack_48 = 0;
      local_58 = 0;
      lVar2 = thunk_FUN_ffff0000b804bccc(lVar4);
      if (lVar2 != 0) {
        thunk_FUN_ffff0000b804d070(lVar2,0,lVar4);
        local_58 = uVar7 & 0xffffffff;
        local_50 = uVar8;
        lStack_48 = lVar2;
      }
      iVar3 = *param_1;
      if (0 < iVar3) {
        lVar4 = 0;
        do {
          lVar5 = *(long *)(*(long *)(param_1 + 4) + lVar4 * 8);
          if (lVar5 != 0) {
            plVar1 = (long *)FUN_ffff0000b807a0ac(&local_58,lVar5);
            if (plVar1 == (long *)0x0) {
              FUN_ffff0000b80777d0
                        (s_ERROR___s____ffff0000b809976e,
                         s__ufdt_prop_dict_enlarge_if_neede_ffff0000b808cce2);
              FUN_ffff0000b80777d0(s_ufdt_prop_dict__failed_to_find_n_ffff0000b8086d92);
              thunk_FUN_ffff0000b8082558(lVar2);
              goto LAB_ffff0000b8079f9c;
            }
            *plVar1 = lVar5;
            iVar3 = *param_1;
          }
          lVar4 = lVar4 + 1;
        } while (lVar4 < iVar3);
      }
      thunk_FUN_ffff0000b8082558(*(undefined8 *)(param_1 + 4));
      uVar8 = 0;
      *param_1 = iVar6;
      *(long *)(param_1 + 4) = lVar2;
    }
    else {
      uVar8 = 0;
    }
  }
  return uVar8;
}


