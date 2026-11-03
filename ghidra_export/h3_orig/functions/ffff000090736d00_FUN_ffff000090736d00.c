// ffff000090736d00  size=424  FUN_ffff000090736d00

void FUN_ffff000090736d00(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  char *pcVar4;
  uint local_14;
  
  pcVar4 = s_mediatek_pmic_auxadc_ffff0000907fb769;
  iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_pmic_auxadc_ffff0000907fb769);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s_dts_node_not_found__ret__d_ffff0000907d2ca8,
               s_mediatek_pmic_auxadc_ffff0000907fb769,iVar2);
    pcVar4 = s_mediatek_pmic_auxadc_ffff0000907d7a69;
    iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_pmic_auxadc_ffff0000907d7a69);
    if (iVar2 < 0) {
      FUN_ffff00009077de54
                (s__s_dts_node_not_found__ret__d_ffff0000907d2ca8,
                 s_mediatek_pmic_auxadc_ffff0000907d7a69,iVar2);
      pcVar4 = s_mediatek_pmic_auxadc_1_ffff00009080256f;
      iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_pmic_auxadc_1_ffff00009080256f);
      if (iVar2 < 0) {
        FUN_ffff00009077de54
                  (s__s_dts_node_not_found__ret__d_ffff0000907d2ca8,
                   s_mediatek_pmic_auxadc_1_ffff00009080256f,iVar2);
        pcVar4 = s_mediatek_pmic_auxadc_2_ffff0000907f9bb1;
        iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_pmic_auxadc_2_ffff0000907f9bb1);
        if (iVar2 < 0) {
          FUN_ffff00009077de54
                    (s__s_dts_node_not_found__ret__d_ffff0000907d2ca8,
                     s_mediatek_pmic_auxadc_2_ffff0000907f9bb1,iVar2);
          FUN_ffff00009077de54(s_no_match_compatible_node__ret__d_ffff0000907e61a6,iVar2);
          goto LAB_ffff000090736dec;
        }
      }
    }
  }
  FUN_ffff00009077de54(s_get__s_dts_node__ffff0000907e6194,pcVar4);
LAB_ffff000090736dec:
  iVar3 = FUN_ffff00009076d54c(param_1,iVar2,s_imix_r_ffff0000907e4985);
  if (iVar3 < 0) {
    iVar3 = FUN_ffff00009076d54c(param_1,iVar2,s_imix_r_ffff0000907f2b54);
    if (iVar3 < 0) {
      FUN_ffff00009077de54(s_imix_r_imix_r_node_not_found__re_ffff0000907fb743,iVar3);
      return;
    }
  }
  uVar1 = (DAT_ffff000090819308 & 0xff00ff00) >> 8 | (DAT_ffff000090819308 & 0xff00ff) << 8;
  local_14 = uVar1 >> 0x10 | uVar1 << 0x10;
  FUN_ffff00009076e40c(param_1,iVar3,&DAT_ffff0000907f2b5b,&local_14,4);
  return;
}


