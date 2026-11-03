
undefined8
FUN_ffff00009072e2bc
          (undefined8 param_1,undefined8 *param_2,undefined8 *param_3,undefined8 *param_4,
          undefined4 *param_5)

{
  undefined **ppuVar1;
  int iVar2;
  undefined8 uVar3;
  long lVar4;
  
  iVar2 = FUN_ffff00009071c708();
  ppuVar1 = &PTR_s_ap_md_c_smem_ffff0000908190e0;
  if (iVar2 != 0) {
    ppuVar1 = &PTR_s_ap_md_c_smem_ffff000090819018;
  }
  if (*ppuVar1 == (undefined *)0x0) {
LAB_ffff00009072e368:
    uVar3 = 0xffffffff;
  }
  else {
    iVar2 = FUN_ffff00009077f0ac(param_1);
    if (iVar2 == 0) {
      lVar4 = 0;
    }
    else {
      iVar2 = FUN_ffff00009077f0ac(param_1,s_ap_md_nc_smem_ffff0000907e2a98);
      if (iVar2 == 0) {
        lVar4 = 1;
      }
      else {
        iVar2 = FUN_ffff00009077f0ac(param_1,s_md_mem_usage_ffff0000907d0f95);
        if (iVar2 == 0) {
          lVar4 = 2;
        }
        else {
          iVar2 = FUN_ffff00009077f0ac(param_1,s_ccci_tag_mem_ffff0000907f62a7);
          if (iVar2 == 0) {
            lVar4 = 3;
          }
          else {
            iVar2 = FUN_ffff00009077f0ac(param_1,s_md1_sib_mem_ffff0000907ec394);
            if (iVar2 != 0) goto LAB_ffff00009072e368;
            lVar4 = 4;
          }
        }
      }
    }
    if (param_3 != (undefined8 *)0x0) {
      *param_3 = ppuVar1[lVar4 * 5 + 3];
    }
    if (param_4 != (undefined8 *)0x0) {
      *param_4 = ppuVar1[lVar4 * 5 + 4];
    }
    if (param_5 != (undefined4 *)0x0) {
      *param_5 = *(undefined4 *)(ppuVar1 + lVar4 * 5 + 1);
    }
    if (param_2 == (undefined8 *)0x0) {
      uVar3 = 0;
    }
    else {
      uVar3 = 0;
      *param_2 = ppuVar1[lVar4 * 5 + 2];
    }
  }
  return uVar3;
}


