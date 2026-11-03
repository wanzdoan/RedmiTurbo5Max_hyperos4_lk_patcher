// File offset entry: 0x14524

undefined8 FUN_ffff0000b8014524(void)

{
  byte *pbVar1;
  char cVar2;
  int iVar3;
  long lVar4;
  char *pcVar5;
  char *pcVar6;
  char local_c0 [72];
  char local_78 [72];
  undefined4 local_14;
  
  pcVar6 = local_c0;
  if ((DAT_ffff0000b811af8c & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_dconfig_ffff0000b809f483,0x106,
               s_g_env_init_done____DCONFIG_VALID_ffff0000b809c302);
  }
  if (DAT_ffff0000b811af90 == '\x01') {
    iVar3 = thunk_FUN_ffff0000b805b018(s_boot_para_ffff0000b809b605);
    if (iVar3 == 0) {
      return 1;
    }
    local_78[0x40] = 0;
    local_78[0x30] = '\0';
    local_78[0x31] = '\0';
    local_78[0x32] = '\0';
    local_78[0x33] = '\0';
    local_78[0x34] = '\0';
    local_78[0x35] = '\0';
    local_78[0x36] = '\0';
    local_78[0x37] = '\0';
    local_78[0x38] = '\0';
    local_78[0x39] = '\0';
    local_78[0x3a] = '\0';
    local_78[0x3b] = '\0';
    local_78[0x3c] = '\0';
    local_78[0x3d] = '\0';
    local_78[0x3e] = '\0';
    local_78[0x3f] = '\0';
    pcVar5 = local_78;
    local_78[0x20] = '\0';
    local_78[0x21] = '\0';
    local_78[0x22] = '\0';
    local_78[0x23] = '\0';
    local_78[0x24] = '\0';
    local_78[0x25] = '\0';
    local_78[0x26] = '\0';
    local_78[0x27] = '\0';
    local_78[0x28] = '\0';
    local_78[0x29] = '\0';
    local_78[0x2a] = '\0';
    local_78[0x2b] = '\0';
    local_78[0x2c] = '\0';
    local_78[0x2d] = '\0';
    local_78[0x2e] = '\0';
    local_78[0x2f] = '\0';
    local_78[0x10] = '\0';
    local_78[0x11] = '\0';
    local_78[0x12] = '\0';
    local_78[0x13] = '\0';
    local_78[0x14] = '\0';
    local_78[0x15] = '\0';
    local_78[0x16] = '\0';
    local_78[0x17] = '\0';
    local_78[0x18] = '\0';
    local_78[0x19] = '\0';
    local_78[0x1a] = '\0';
    local_78[0x1b] = '\0';
    local_78[0x1c] = '\0';
    local_78[0x1d] = '\0';
    local_78[0x1e] = '\0';
    local_78[0x1f] = '\0';
    local_78[0] = '\0';
    local_78[1] = '\0';
    local_78[2] = '\0';
    local_78[3] = '\0';
    local_78[4] = '\0';
    local_78[5] = '\0';
    local_78[6] = '\0';
    local_78[7] = '\0';
    local_78[8] = '\0';
    local_78[9] = '\0';
    local_78[10] = '\0';
    local_78[0xb] = '\0';
    local_78[0xc] = '\0';
    local_78[0xd] = '\0';
    local_78[0xe] = '\0';
    local_78[0xf] = '\0';
    local_c0[0] = '\0';
    local_c0[1] = '\0';
    local_c0[2] = '\0';
    local_c0[3] = '\0';
    local_c0[4] = '\0';
    local_c0[5] = '\0';
    local_c0[6] = '\0';
    local_c0[7] = '\0';
    local_c0[8] = '\0';
    local_c0[9] = '\0';
    local_c0[10] = '\0';
    local_c0[0xb] = '\0';
    local_c0[0xc] = '\0';
    local_c0[0xd] = '\0';
    local_c0[0xe] = '\0';
    local_c0[0xf] = '\0';
    local_c0[0x10] = '\0';
    local_c0[0x11] = '\0';
    local_c0[0x12] = '\0';
    local_c0[0x13] = '\0';
    local_c0[0x14] = '\0';
    local_c0[0x15] = '\0';
    local_c0[0x16] = '\0';
    local_c0[0x17] = '\0';
    local_c0[0x18] = '\0';
    local_c0[0x19] = '\0';
    local_c0[0x1a] = '\0';
    local_c0[0x1b] = '\0';
    local_c0[0x1c] = '\0';
    local_c0[0x1d] = '\0';
    local_c0[0x1e] = '\0';
    local_c0[0x1f] = '\0';
    local_c0[0x20] = '\0';
    local_c0[0x21] = '\0';
    local_c0[0x22] = '\0';
    local_c0[0x23] = '\0';
    local_c0[0x24] = '\0';
    local_c0[0x25] = '\0';
    local_c0[0x26] = '\0';
    local_c0[0x27] = '\0';
    local_c0[0x28] = '\0';
    local_c0[0x29] = '\0';
    local_c0[0x2a] = '\0';
    local_c0[0x2b] = '\0';
    local_c0[0x2c] = '\0';
    local_c0[0x2d] = '\0';
    local_c0[0x2e] = '\0';
    local_c0[0x2f] = '\0';
    local_c0[0x30] = '\0';
    local_c0[0x31] = '\0';
    local_c0[0x32] = '\0';
    local_c0[0x33] = '\0';
    local_c0[0x34] = '\0';
    local_c0[0x35] = '\0';
    local_c0[0x36] = '\0';
    local_c0[0x37] = '\0';
    local_c0[0x38] = '\0';
    local_c0[0x39] = '\0';
    local_c0[0x3a] = '\0';
    local_c0[0x3b] = '\0';
    local_c0[0x3c] = '\0';
    local_c0[0x3d] = '\0';
    local_c0[0x3e] = '\0';
    local_c0[0x3f] = '\0';
    local_c0[0x40] = 0;
    FUN_ffff0000b804d070(local_78,0,0x41);
    lVar4 = 0;
    do {
      pbVar1 = &DAT_ffff0000b811afcc + lVar4;
      lVar4 = lVar4 + 1;
      cVar2 = s_0123456789ABCDEF_ffff0000b809d56a[(ulong)*pbVar1 & 0xf];
      *pcVar5 = s_0123456789ABCDEF_ffff0000b809d56a[*pbVar1 >> 4];
      pcVar5[1] = cVar2;
      pcVar5 = pcVar5 + 2;
    } while (lVar4 != 0x20);
    FUN_ffff0000b804d070(local_c0,0,0x41);
    lVar4 = 0;
    do {
      pbVar1 = &DAT_ffff0000b811afec + lVar4;
      lVar4 = lVar4 + 1;
      cVar2 = s_0123456789ABCDEF_ffff0000b809d56a[(ulong)*pbVar1 & 0xf];
      *pcVar6 = s_0123456789ABCDEF_ffff0000b809d56a[*pbVar1 >> 4];
      pcVar6[1] = cVar2;
      pcVar6 = pcVar6 + 2;
    } while (lVar4 != 0x20);
    FUN_ffff0000b804c33c
              (s__s__d__socid_cert__s_device__s_ffff0000b808f9d4,
               s_dconfig_socid_match_ffff0000b8089a25,0x10e,local_78,local_c0);
    lVar4 = 0;
    do {
      if ((&DAT_ffff0000b811afcc)[lVar4] != '\0') {
        iVar3 = FUN_ffff0000b804cd08(&DAT_ffff0000b811afcc,&DAT_ffff0000b811afec,0x20);
        if (iVar3 == 0) {
          return 1;
        }
        FUN_ffff0000b804c33c
                  (s__s__d__dconfig_cert_device_socid_ffff0000b80960f4,
                   s_dconfig_socid_match_ffff0000b8089a25,0x115);
        local_14 = 0;
        iVar3 = FUN_ffff0000b802c7fc(0,&local_14);
        if (iVar3 == 0) {
          if (local_14._1_1_ != -0x10) {
            FUN_ffff0000b804c33c(s_dconfig_cert_device_socid_mismat_ffff0000b809425a);
            return 1;
          }
          return 0;
        }
        FUN_ffff0000b804c33c(s_Read_efuse_fail_in_dconfig__0x_x_ffff0000b8089a39,iVar3);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_platform_mediatek_common_dconfig_ffff0000b809f483,0x121,&DAT_ffff0000b8086efb);
      }
      lVar4 = lVar4 + 1;
    } while (lVar4 != 0x20);
    FUN_ffff0000b804c33c
              (s__s__d__Null_cert_socid_ffff0000b809a7fa,s_dconfig_socid_match_ffff0000b8089a25,
               0x111);
  }
  return 0;
}


