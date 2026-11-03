// ffff000090772630  size=372  FUN_ffff000090772630

undefined8 FUN_ffff000090772630(int *param_1,long param_2)

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
  
  plVar1 = (long *)FUN_ffff0000907727a4();
  if (plVar1 == (long *)0x0) {
    FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_prop_dict_add_ffff0000907fe1b6);
    FUN_ffff00009076fec8(s_ufdt_prop_dict__failed_to_find_n_ffff0000907f1e28);
LAB_ffff000090772694:
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
      if (param_1 == (int *)0x0) goto LAB_ffff000090772694;
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
      lVar2 = thunk_FUN_ffff00009077d514(lVar4);
      if (lVar2 != 0) {
        thunk_FUN_ffff00009077ee50(lVar2,0,lVar4);
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
            plVar1 = (long *)FUN_ffff0000907727a4(&local_58,lVar5);
            if (plVar1 == (long *)0x0) {
              FUN_ffff00009076fec8
                        (s_ERROR___s____ffff0000907f36ba,
                         s__ufdt_prop_dict_enlarge_if_neede_ffff0000907dd2f4);
              FUN_ffff00009076fec8(s_ufdt_prop_dict__failed_to_find_n_ffff0000907d1dc2);
              thunk_FUN_ffff0000907c314c(lVar2);
              goto LAB_ffff000090772694;
            }
            *plVar1 = lVar5;
            iVar3 = *param_1;
          }
          lVar4 = lVar4 + 1;
        } while (lVar4 < iVar3);
      }
      thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 4));
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


