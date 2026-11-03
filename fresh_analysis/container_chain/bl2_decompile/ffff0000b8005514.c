
int FUN_ffff0000b8005514(uint param_1,int *param_2)

{
  int iVar1;
  char *pcVar2;
  char *pcVar3;
  undefined8 uVar4;
  uint uVar5;
  uint local_48;
  uint local_44;
  ushort local_28 [2];
  uint local_24;
  
  uVar4 = DAT_ffff0000b810c070;
  local_48 = 0;
  local_44 = 0;
  iVar1 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x14f,&local_44);
  if (-1 < iVar1) {
    local_44 = local_44 & 0xffffffe0 | param_1 & 0x1f;
    iVar1 = FUN_ffff0000b80211d4(uVar4,0x14f);
    if (iVar1 == 0) {
      FUN_ffff0000b804b704(7000);
      FUN_ffff0000b804b704(1000);
      iVar1 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x14f,&local_48);
      if ((iVar1 != 0) || (local_48 = local_48 & 0x1f, local_48 != 0)) {
        FUN_ffff0000b804b704(1000);
        iVar1 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x14f,&local_48);
        if ((iVar1 != 0) || (local_48 = local_48 & 0x1f, local_48 != 0)) {
          FUN_ffff0000b804b704(1000);
          iVar1 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x14f,&local_48);
          if ((iVar1 != 0) || (local_48 = local_48 & 0x1f, local_48 != 0)) {
            FUN_ffff0000b804b704(1000);
            iVar1 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x14f,&local_48);
            if ((iVar1 != 0) || (local_48 = local_48 & 0x1f, local_48 != 0)) {
              FUN_ffff0000b804b704(1000);
              iVar1 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x14f,&local_48);
              if ((iVar1 != 0) || (local_48 = local_48 & 0x1f, local_48 != 0)) {
                FUN_ffff0000b804c33c
                          (s__s__d__Failed_to_start_adc_ffff0000b808f7e2,
                           s_mt6379_get_adc_ffff0000b8094032,0x169);
                return -0x21;
              }
            }
          }
        }
      }
      uVar4 = DAT_ffff0000b810c070;
      local_28[0] = 0;
      local_24 = 0;
      iVar1 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x155,&local_24);
      if (-1 < iVar1) {
        local_24 = local_24 & 0xffffffe0 | param_1 & 0x1f;
        iVar1 = FUN_ffff0000b80211d4(uVar4,0x155);
        if (iVar1 == 0) {
          FUN_ffff0000b804b704(1000);
          iVar1 = FUN_ffff0000b802127c(DAT_ffff0000b810c070,0x156,local_28,2);
          if (iVar1 == 0) {
            if ((0x946 < ((uint)(local_28[0] >> 8) | (local_28[0] & 0xff00ff) << 8) >> 3) ||
               ((uVar5 = (uint)(local_28[0] >> 8) | (local_28[0] & 0xff00ff) << 8, param_1 == 1 &&
                (uVar5 == 0x200)))) {
              uVar5 = 0;
            }
            iVar1 = uVar5 - 0x40;
            if (param_1 != 8) {
              iVar1 = uVar5 * 1000;
            }
            *param_2 = iVar1;
            return 0;
          }
        }
      }
      pcVar2 = s__s__d__Failed_to_get_adc_report_ffff0000b8094007;
      pcVar3 = s_mt6379_get_adc_ffff0000b8094032;
      uVar4 = 0x170;
      goto LAB_ffff0000b8005698;
    }
  }
  pcVar2 = s__s__d__Failed_to_select_adc_ones_ffff0000b809067e;
  pcVar3 = s_mt6379_config_adc_oneshot_ffff0000b80989ae;
  uVar4 = 0xe7;
LAB_ffff0000b8005698:
  FUN_ffff0000b804c33c(pcVar2,pcVar3,uVar4);
  return iVar1;
}


