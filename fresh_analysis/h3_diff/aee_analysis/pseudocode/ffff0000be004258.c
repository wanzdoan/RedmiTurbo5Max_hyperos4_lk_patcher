
int FUN_ffff0000be004258(int *param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  char *pcVar5;
  undefined8 uVar6;
  uint local_40;
  uint local_3c;
  ushort local_38 [2];
  uint local_34;
  
  local_40 = 0;
  lVar3 = FUN_ffff0000be02956c(s_mt6379_ffff0000be0b5f6e);
  if (lVar3 != 0) {
    DAT_ffff0000be0f9050 = lVar3;
  }
  lVar3 = DAT_ffff0000be0f9050;
  local_3c = 0;
  iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x14f,&local_3c);
  if (-1 < iVar2) {
    local_3c = local_3c & 0xffffffe0 | 1;
    iVar2 = FUN_ffff0000be029920(lVar3,0x14f);
    if (iVar2 == 0) {
      FUN_ffff0000be05ede4(7000);
      FUN_ffff0000be05ede4(1000);
      iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x14f,&local_40);
      if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
        FUN_ffff0000be05ede4(1000);
        iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x14f,&local_40);
        if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
          FUN_ffff0000be05ede4(1000);
          iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x14f,&local_40);
          if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
            FUN_ffff0000be05ede4(1000);
            iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x14f,&local_40);
            if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
              FUN_ffff0000be05ede4(1000);
              iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x14f,&local_40);
              if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
                FUN_ffff0000be05fd98
                          (s__s__d__Failed_to_start_adc_ffff0000be0b07c2,
                           s_mt6379_get_adc_ext_ffff0000be0bb9f2,0x140);
                return -0x21;
              }
            }
          }
        }
      }
      lVar3 = DAT_ffff0000be0f9050;
      local_38[0] = 0;
      local_34 = 0;
      iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x155,&local_34);
      if (-1 < iVar2) {
        local_34 = local_34 & 0xffffffe0 | 1;
        iVar2 = FUN_ffff0000be029920(lVar3,0x155);
        if (iVar2 == 0) {
          FUN_ffff0000be05ede4(1000);
          iVar2 = FUN_ffff0000be0299c8(DAT_ffff0000be0f9050,0x156,local_38,2);
          if (iVar2 == 0) {
            uVar1 = (uint)(local_38[0] >> 8) | (local_38[0] & 0xff00ff) << 8;
            iVar2 = 0;
            if (uVar1 != 0x200 &&
                ((uint)(local_38[0] >> 8) | (local_38[0] & 0xff00ff) << 8) >> 3 < 0x947) {
              iVar2 = uVar1 * 1000;
            }
            *param_1 = iVar2;
            return 0;
          }
        }
      }
      pcVar4 = s__s__d__Failed_to_get_adc_report_ffff0000be0b5f75;
      pcVar5 = s_mt6379_get_adc_ext_ffff0000be0bb9f2;
      uVar6 = 0x147;
      goto LAB_ffff0000be0043fc;
    }
  }
  pcVar4 = s__s__d__Failed_to_select_adc_ones_ffff0000be0b18ca;
  pcVar5 = s_mt6379_config_adc_oneshot_ffff0000be0bba05;
  uVar6 = 0xe7;
LAB_ffff0000be0043fc:
  FUN_ffff0000be05fd98(pcVar4,pcVar5,uVar6);
  return iVar2;
}


