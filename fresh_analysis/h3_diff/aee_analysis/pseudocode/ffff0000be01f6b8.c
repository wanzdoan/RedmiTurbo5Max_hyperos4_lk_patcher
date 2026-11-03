
void FUN_ffff0000be01f6b8(void)

{
  byte *pbVar1;
  int iVar2;
  char *pcVar3;
  undefined8 uVar4;
  long lVar5;
  int iVar6;
  char *pcVar7;
  long lVar8;
  uint uVar9;
  uint uVar10;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  if (DAT_ffff0000be1176a0 == '\x01') {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_common_dconfig_ffff0000be0c3eb5,0x99,
               s_g_env_init_done____DCONFIG_VALID_ffff0000be0ae63e);
  }
  DAT_ffff0000be1176a0 = 1;
  DAT_ffff0000be1176a4 = 0;
  DAT_ffff0000be1176a8 = FUN_ffff0000be05f1bc(0x600);
  if (DAT_ffff0000be1176a8 == 0) {
    pcVar3 = s__s__d__malloc_failed_ffff0000be0b876b;
    uVar4 = 0xa0;
  }
  else {
    FUN_ffff0000be060ae0(DAT_ffff0000be1176a8,0,0x600);
    iVar2 = FUN_ffff0000be01fef0
                      (s_dconfig_ffff0000be0a3323,DAT_ffff0000be1176a8,0x600,&local_80,
                       &PTR_DAT_ffff0000be0d64c0);
    lVar5 = DAT_ffff0000be1176a8;
    if (iVar2 != 0x600) {
      FUN_ffff0000be05fd98
                (s__s__d__load_dconfig_partition_fa_ffff0000be0b2d63,
                 s_dconfig_initenv_ffff0000be0ab48f,0xa7,iVar2);
      goto LAB_ffff0000be01f80c;
    }
    DAT_ffff0000be1176b8 = (char *)(DAT_ffff0000be1176a8 + 0x208);
    FUN_ffff0000be0607b0(&DAT_ffff0000be1176b0,DAT_ffff0000be1176a8 + 0x200,8);
    FUN_ffff0000be0607b0(&DAT_ffff0000be1176c0,lVar5 + 0x5f4,8);
    iVar2 = FUN_ffff0000be060cb8(&DAT_ffff0000be1176b0,s_ENV_v1_ffff0000be0a6bc5);
    if ((iVar2 == 0) &&
       (iVar2 = FUN_ffff0000be060cb8(&DAT_ffff0000be1176c0,s_ENV_v1_ffff0000be0a6bc5),
       pcVar3 = DAT_ffff0000be1176b8, iVar2 == 0)) {
      lVar8 = 0;
      iVar2 = 0;
      iVar6 = 0;
      DAT_ffff0000be1176c8 = *(int *)(lVar5 + 0x5fc);
      do {
        pbVar1 = (byte *)(DAT_ffff0000be1176b8 + lVar8);
        lVar8 = lVar8 + 2;
        iVar2 = iVar2 + (uint)*pbVar1;
        iVar6 = iVar6 + (uint)pbVar1[1];
      } while (lVar8 != 0x3ec);
      if (iVar6 + iVar2 != DAT_ffff0000be1176c8) {
        FUN_ffff0000be05fd98
                  (s__s__d__checksum_mismatch_s__d_d___ffff0000be0a6bcc,
                   s_dconfig_initenv_ffff0000be0ab48f,0xb4);
        goto LAB_ffff0000be01f80c;
      }
      FUN_ffff0000be05fd98
                (s__s__d__print_env_data_ffff0000be0c3ee0,s_dconfig_printenv_ffff0000be0ab49f,0x85);
      if (*DAT_ffff0000be1176b8 != '\0') {
        lVar5 = 0;
        uVar9 = 0;
        do {
          lVar8 = (long)(int)uVar9;
          uVar10 = uVar9;
          if ((int)uVar9 < 0x3ed) {
            uVar10 = 0x3ec;
          }
          pcVar7 = DAT_ffff0000be1176b8 + lVar8;
          uVar9 = uVar9 + 1;
          lVar8 = (ulong)uVar10 - lVar8;
          do {
            pcVar7 = pcVar7 + 1;
            if (lVar8 == 0) goto LAB_ffff0000be01f930;
            uVar9 = uVar9 + 1;
            lVar8 = lVar8 + -1;
          } while (*pcVar7 != '\0');
          FUN_ffff0000be05fd98
                    (s__s__d___>__s_ffff0000be0a7d53,s_dconfig_printenv_ffff0000be0ab49f,0x8f,
                     pcVar3 + lVar5);
          lVar5 = (long)(int)uVar9;
        } while (DAT_ffff0000be1176b8[lVar5] != '\0');
      }
LAB_ffff0000be01f930:
      DAT_ffff0000be1176d0 = FUN_ffff0000be05f1bc(0x20000);
      if (DAT_ffff0000be1176d0 == 0) {
        pcVar3 = s__s__d__malloc_failed_ffff0000be0b876b;
        uVar4 = 0xd3;
        DAT_ffff0000be1176d0 = 0;
      }
      else {
        iVar2 = FUN_ffff0000be01fef0
                          (s_dconfig_dt_ffff0000be0b760d,DAT_ffff0000be1176d0,0x20000,&local_a0,
                           &PTR_DAT_ffff0000be0d64c0);
        if (iVar2 < 1) {
          FUN_ffff0000be05fd98
                    (s__s__d__No_dconfig_dt_image_ffff0000be0bcfdf,
                     s_dconfig_initenv_ffff0000be0ab48f,0xcd);
          thunk_FUN_ffff0000be09937c(DAT_ffff0000be1176d0);
          DAT_ffff0000be1176d0 = 0;
          DAT_ffff0000be1176d8 = 0;
        }
        else {
          DAT_ffff0000be1176d8 = iVar2;
          FUN_ffff0000be05fd98
                    (s__s__d__dconfig_dtb_image_found___ffff0000be0a8f26,
                     s_dconfig_initenv_ffff0000be0ab48f,0xc6,iVar2);
          DAT_ffff0000be1176d0 = FUN_ffff0000be05f2c0(DAT_ffff0000be1176d0,DAT_ffff0000be1176d8);
          if (DAT_ffff0000be1176d0 == 0) {
            pcVar3 = s__s__d__dconfig_dtb_buffer_resize_ffff0000be0c064c;
            uVar4 = 0xc9;
            goto LAB_ffff0000be01f7e8;
          }
        }
        iVar2 = FUN_ffff0000be060778(&local_80,&local_a0,0x20);
        if (iVar2 == 0) {
          FUN_ffff0000be0607b0(&DAT_ffff0000be1176dc,&local_80,0x20);
          DAT_ffff0000be1176a4 = 1;
          FUN_ffff0000be05fd98
                    (s__s__d__dconfig_initialize_succes_ffff0000be0af749,
                     s_dconfig_initenv_ffff0000be0ab48f,0xde);
          return;
        }
        pcVar3 = s__s__d__cert_socid_mismatch_ffff0000be0c614d;
        uVar4 = 0xd8;
      }
    }
    else {
      pcVar3 = s__s__d__ENV_SIG_is_wrong_ffff0000be0bcfc6;
      uVar4 = 0xb8;
    }
  }
LAB_ffff0000be01f7e8:
  FUN_ffff0000be05fd98(pcVar3,s_dconfig_initenv_ffff0000be0ab48f,uVar4);
LAB_ffff0000be01f80c:
  thunk_FUN_ffff0000be09937c(DAT_ffff0000be1176a8);
  DAT_ffff0000be1176d8 = 0;
  thunk_FUN_ffff0000be09937c(DAT_ffff0000be1176d0);
  DAT_ffff0000be1176a8 = 0;
  return;
}


