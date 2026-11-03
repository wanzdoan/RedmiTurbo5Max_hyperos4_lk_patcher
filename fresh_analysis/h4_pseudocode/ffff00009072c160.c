
undefined8 FUN_ffff00009072c160(int param_1,undefined8 *param_2,int *param_3,undefined4 *param_4)

{
  char *pcVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  undefined4 *puVar4;
  ulong uVar5;
  
  if ((DAT_ffff00009086d6fc & 1) == 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d___ccci__s_share_mem_ffff0000907d5f4e,
               s_ccci_get_smem_info_ffff0000907ef867,0x428,s_ccci_get_smem_info_ffff0000907ef867);
    return 0xfffffffe;
  }
  if (DAT_ffff00009086d700 == 0) {
    pcVar1 = s__CCCI___s__d___ccci__s_share_mem_ffff0000907fef01;
    uVar2 = 0x42d;
  }
  else {
    if (DAT_ffff00009086d708 != 0) {
      uVar5 = (ulong)DAT_ffff00009086d710;
      if (DAT_ffff00009086d710 != 0) {
        puVar4 = (undefined4 *)(DAT_ffff00009086d700 + 0x18);
        do {
          if ((puVar4[-2] == param_1) && ((*(byte *)(puVar4 + 2) >> 2 & 1) == 0)) {
            if (param_2 != (undefined8 *)0x0) {
              *param_2 = *(undefined8 *)(puVar4 + -6);
            }
            if (param_3 != (int *)0x0) {
              *param_3 = puVar4[3] + 0x40000000;
            }
            if (param_4 == (undefined4 *)0x0) {
              return 0;
            }
            uVar3 = *puVar4;
            goto LAB_ffff00009072c288;
          }
          puVar4 = puVar4 + 10;
          uVar5 = uVar5 - 1;
        } while (uVar5 != 0);
      }
      uVar5 = (ulong)DAT_ffff00009086d714;
      if (DAT_ffff00009086d714 == 0) {
        return 0xffffffff;
      }
      puVar4 = (undefined4 *)(DAT_ffff00009086d708 + 0x18);
      while ((puVar4[-2] != param_1 || ((*(byte *)(puVar4 + 2) >> 2 & 1) != 0))) {
        puVar4 = puVar4 + 10;
        uVar5 = uVar5 - 1;
        if (uVar5 == 0) {
          return 0xffffffff;
        }
      }
      if (param_2 != (undefined8 *)0x0) {
        *param_2 = *(undefined8 *)(puVar4 + -6);
      }
      if (param_3 != (int *)0x0) {
        *param_3 = puVar4[3] + 0x40000000;
      }
      if (param_4 == (undefined4 *)0x0) {
        return 0;
      }
      uVar3 = *puVar4;
LAB_ffff00009072c288:
      *param_4 = uVar3;
      return 0;
    }
    pcVar1 = s__CCCI___s__d___ccci__s_share_mem_ffff0000907f29e7;
    uVar2 = 0x431;
  }
  FUN_ffff000090730228
            (pcVar1,s_ccci_get_smem_info_ffff0000907ef867,uVar2,
             s_ccci_get_smem_info_ffff0000907ef867);
  return 0xffffffff;
}


