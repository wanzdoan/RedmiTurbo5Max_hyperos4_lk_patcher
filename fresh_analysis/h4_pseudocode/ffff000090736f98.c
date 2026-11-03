
void FUN_ffff000090736f98(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  char *pcVar4;
  uint local_14;
  
  pcVar4 = s_mediatek_pmic_auxadc_ffff0000907fb905;
  iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_pmic_auxadc_ffff0000907fb905);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s_dts_node_not_found__ret__d_ffff0000907d2ce5,
               s_mediatek_pmic_auxadc_ffff0000907fb905,iVar2);
    pcVar4 = s_mediatek_pmic_auxadc_ffff0000907d7aff;
    iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_pmic_auxadc_ffff0000907d7aff);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s__s_dts_node_not_found__ret__d_ffff0000907d2ce5,
                 s_mediatek_pmic_auxadc_ffff0000907d7aff,iVar2);
      pcVar4 = s_mediatek_pmic_auxadc_1_ffff000090802773;
      iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_pmic_auxadc_1_ffff000090802773);
      if (iVar2 < 0) {
        FUN_ffff00009077e11c
                  (s__s_dts_node_not_found__ret__d_ffff0000907d2ce5,
                   s_mediatek_pmic_auxadc_1_ffff000090802773,iVar2);
        pcVar4 = s_mediatek_pmic_auxadc_2_ffff0000907f9d4d;
        iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_pmic_auxadc_2_ffff0000907f9d4d);
        if (iVar2 < 0) {
          FUN_ffff00009077e11c
                    (s__s_dts_node_not_found__ret__d_ffff0000907d2ce5,
                     s_mediatek_pmic_auxadc_2_ffff0000907f9d4d,iVar2);
          FUN_ffff00009077e11c(s_no_match_compatible_node__ret__d_ffff0000907e62ba,iVar2);
          goto LAB_ffff000090737084;
        }
      }
    }
  }
  FUN_ffff00009077e11c(s_get__s_dts_node__ffff0000907e62a8,pcVar4);
LAB_ffff000090737084:
  iVar3 = FUN_ffff00009076d814(param_1,iVar2,s_imix_r_ffff0000907e4a89);
  if (iVar3 < 0) {
    iVar3 = FUN_ffff00009076d814(param_1,iVar2,s_imix_r_ffff0000907f2ce0);
    if (iVar3 < 0) {
      FUN_ffff00009077e11c(s_imix_r_imix_r_node_not_found__re_ffff0000907fb8df,iVar3);
      return;
    }
  }
  uVar1 = (DAT_ffff000090819328 & 0xff00ff00) >> 8 | (DAT_ffff000090819328 & 0xff00ff) << 8;
  local_14 = uVar1 >> 0x10 | uVar1 << 0x10;
  FUN_ffff00009076e6d4(param_1,iVar3,&DAT_ffff0000907f2ce7,&local_14,4);
  return;
}


