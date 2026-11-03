// ffff00009072bec8  size=360  FUN_ffff00009072bec8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072bec8(int param_1,undefined8 *param_2,int *param_3,undefined4 *param_4)

{
  char *pcVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  undefined4 *puVar4;
  ulong uVar5;
  
  if ((DAT_ffff00009086d664 & 1) == 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d___ccci__s_share_mem_ffff0000907d5eb8,
               s_ccci_get_smem_info_ffff0000907ef6df,0x428,s_ccci_get_smem_info_ffff0000907ef6df);
    return 0xfffffffe;
  }
  if (_DAT_ffff00009086d668 == 0) {
    pcVar1 = s__CCCI___s__d___ccci__s_share_mem_ffff0000907fed59;
    uVar2 = 0x42d;
  }
  else {
    if (_DAT_ffff00009086d670 != 0) {
      uVar5 = (ulong)_DAT_ffff00009086d678;
      if (_DAT_ffff00009086d678 != 0) {
        puVar4 = (undefined4 *)(_DAT_ffff00009086d668 + 0x18);
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
            goto LAB_ffff00009072bff0;
          }
          puVar4 = puVar4 + 10;
          uVar5 = uVar5 - 1;
        } while (uVar5 != 0);
      }
      uVar5 = (ulong)_DAT_ffff00009086d67c;
      if (_DAT_ffff00009086d67c == 0) {
        return 0xffffffff;
      }
      puVar4 = (undefined4 *)(_DAT_ffff00009086d670 + 0x18);
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
LAB_ffff00009072bff0:
      *param_4 = uVar3;
      return 0;
    }
    pcVar1 = s__CCCI___s__d___ccci__s_share_mem_ffff0000907f2844;
    uVar2 = 0x431;
  }
  FUN_ffff00009072ff90
            (pcVar1,s_ccci_get_smem_info_ffff0000907ef6df,uVar2,
             s_ccci_get_smem_info_ffff0000907ef6df);
  return 0xffffffff;
}


