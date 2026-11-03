// ffff00009070479c  size=632  FUN_ffff00009070479c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070479c(int *param_1)

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
  lVar3 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea425);
  if (lVar3 != 0) {
    _DAT_ffff00009083d0e0 = lVar3;
  }
  lVar3 = _DAT_ffff00009083d0e0;
  local_3c = 0;
  iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x14f,&local_3c);
  if (-1 < iVar2) {
    local_3c = local_3c & 0xffffffe0 | 1;
    iVar2 = FUN_ffff000090737284(lVar3,0x14f);
    if (iVar2 == 0) {
      FUN_ffff00009077cbd8(7000);
      FUN_ffff00009077cbd8(1000);
      iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x14f,&local_40);
      if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
        FUN_ffff00009077cbd8(1000);
        iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x14f,&local_40);
        if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
          FUN_ffff00009077cbd8(1000);
          iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x14f,&local_40);
          if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
            FUN_ffff00009077cbd8(1000);
            iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x14f,&local_40);
            if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
              FUN_ffff00009077cbd8(1000);
              iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x14f,&local_40);
              if ((iVar2 != 0) || (local_40 = local_40 & 0x1f, local_40 != 0)) {
                FUN_ffff00009077de54
                          (s__s__d__Failed_to_start_adc_ffff0000907e23ae,
                           s_mt6379_get_adc_ext_ffff0000907f2390,0x140);
                return -0x21;
              }
            }
          }
        }
      }
      lVar3 = _DAT_ffff00009083d0e0;
      local_38[0] = 0;
      local_34 = 0;
      iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x155,&local_34);
      if (-1 < iVar2) {
        local_34 = local_34 & 0xffffffe0 | 1;
        iVar2 = FUN_ffff000090737284(lVar3,0x155);
        if (iVar2 == 0) {
          FUN_ffff00009077cbd8(1000);
          iVar2 = FUN_ffff00009073732c(_DAT_ffff00009083d0e0,0x156,local_38,2);
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
      pcVar4 = s__s__d__Failed_to_get_adc_report_ffff0000907ea42c;
      pcVar5 = s_mt6379_get_adc_ext_ffff0000907f2390;
      uVar6 = 0x147;
      goto LAB_ffff000090704940;
    }
  }
  pcVar4 = s__s__d__Failed_to_select_adc_ones_ffff0000907e3f55;
  pcVar5 = s_mt6379_config_adc_oneshot_ffff0000907f23a3;
  uVar6 = 0xe7;
LAB_ffff000090704940:
  FUN_ffff00009077de54(pcVar4,pcVar5,uVar6);
  return iVar2;
}


