// ffff00009072171c  size=980  FUN_ffff00009072171c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072171c(void)

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
  if (DAT_ffff0000908605f8 == '\x01') {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_dconfig_ffff0000907febf4,0x99,
               s_g_env_init_done____DCONFIG_VALID_ffff0000907df5dd);
  }
  DAT_ffff0000908605f8 = 1;
  DAT_ffff0000908605fc = 0;
  _DAT_ffff000090860600 = FUN_ffff00009077d24c(0x600);
  if (_DAT_ffff000090860600 == 0) {
    pcVar3 = s__s__d__malloc_failed_ffff0000907edcba;
    uVar4 = 0xa0;
  }
  else {
    FUN_ffff00009077eb88(_DAT_ffff000090860600,0,0x600);
    iVar2 = FUN_ffff00009072203c
                      (s_dconfig_ffff0000907cf3fd,_DAT_ffff000090860600,0x600,&local_80,
                       &PTR_DAT_ffff000090818490);
    lVar5 = _DAT_ffff000090860600;
    if (iVar2 != 0x600) {
      FUN_ffff00009077de54
                (s__s__d__load_dconfig_partition_fa_ffff0000907e5dd7,
                 s_dconfig_initenv_ffff0000907da784,0xa7,iVar2);
      goto LAB_ffff000090721870;
    }
    _DAT_ffff000090860610 = (char *)(_DAT_ffff000090860600 + 0x208);
    FUN_ffff00009077e858(0xffff000090860608,_DAT_ffff000090860600 + 0x200,8);
    FUN_ffff00009077e858(0xffff000090860618,lVar5 + 0x5f4,8);
    iVar2 = FUN_ffff00009077ede4(0xffff000090860608,s_ENV_v1_ffff0000907d4165);
    if ((iVar2 == 0) &&
       (iVar2 = FUN_ffff00009077ede4(0xffff000090860618,s_ENV_v1_ffff0000907d4165),
       pcVar3 = _DAT_ffff000090860610, iVar2 == 0)) {
      lVar8 = 0;
      iVar2 = 0;
      iVar6 = 0;
      _DAT_ffff000090860620 = *(int *)(lVar5 + 0x5fc);
      do {
        pbVar1 = (byte *)(_DAT_ffff000090860610 + lVar8);
        lVar8 = lVar8 + 2;
        iVar2 = iVar2 + (uint)*pbVar1;
        iVar6 = iVar6 + (uint)pbVar1[1];
      } while (lVar8 != 0x3ec);
      if (iVar6 + iVar2 != _DAT_ffff000090860620) {
        FUN_ffff00009077de54
                  (s__s__d__checksum_mismatch_s__d_d___ffff0000907d416c,
                   s_dconfig_initenv_ffff0000907da784,0xb4);
        goto LAB_ffff000090721870;
      }
      FUN_ffff00009077de54
                (s__s__d__print_env_data_ffff0000907fec1f,s_dconfig_printenv_ffff0000907da794,0x85);
      if (*_DAT_ffff000090860610 != '\0') {
        lVar5 = 0;
        uVar9 = 0;
        do {
          lVar8 = (long)(int)uVar9;
          uVar10 = uVar9;
          if ((int)uVar9 < 0x3ed) {
            uVar10 = 0x3ec;
          }
          pcVar7 = _DAT_ffff000090860610 + lVar8;
          uVar9 = uVar9 + 1;
          lVar8 = (ulong)uVar10 - lVar8;
          do {
            pcVar7 = pcVar7 + 1;
            if (lVar8 == 0) goto LAB_ffff000090721994;
            uVar9 = uVar9 + 1;
            lVar8 = lVar8 + -1;
          } while (*pcVar7 != '\0');
          FUN_ffff00009077de54
                    (s__s__d___>__s_ffff0000907d5c8c,s_dconfig_printenv_ffff0000907da794,0x8f,
                     pcVar3 + lVar5);
          lVar5 = (long)(int)uVar9;
        } while (_DAT_ffff000090860610[lVar5] != '\0');
      }
LAB_ffff000090721994:
      _DAT_ffff000090860628 = FUN_ffff00009077d24c(0x20000);
      if (_DAT_ffff000090860628 == 0) {
        pcVar3 = s__s__d__malloc_failed_ffff0000907edcba;
        uVar4 = 0xd3;
        _DAT_ffff000090860628 = 0;
      }
      else {
        iVar2 = FUN_ffff00009072203c
                          (s_dconfig_dt_ffff0000907ec0e0,_DAT_ffff000090860628,0x20000,&local_a0,
                           &PTR_DAT_ffff000090818490);
        if (iVar2 < 1) {
          FUN_ffff00009077de54
                    (s__s__d__No_dconfig_dt_image_ffff0000907f4358,
                     s_dconfig_initenv_ffff0000907da784,0xcd);
          thunk_FUN_ffff0000907c2ad8(_DAT_ffff000090860628);
          _DAT_ffff000090860628 = 0;
          _DAT_ffff000090860630 = 0;
        }
        else {
          _DAT_ffff000090860630 = iVar2;
          FUN_ffff00009077de54
                    (s__s__d__dconfig_dtb_image_found___ffff0000907d768f,
                     s_dconfig_initenv_ffff0000907da784,0xc6,iVar2);
          _DAT_ffff000090860628 = FUN_ffff00009077d350(_DAT_ffff000090860628,_DAT_ffff000090860630);
          if (_DAT_ffff000090860628 == 0) {
            pcVar3 = s__s__d__dconfig_dtb_buffer_resize_ffff0000907f95a1;
            uVar4 = 0xc9;
            goto LAB_ffff00009072184c;
          }
        }
        iVar2 = FUN_ffff00009077e820(&local_80,&local_a0,0x20);
        if (iVar2 == 0) {
          FUN_ffff00009077e858(0xffff000090860634,&local_80,0x20);
          DAT_ffff0000908605fc = 1;
          FUN_ffff00009077de54
                    (s__s__d__dconfig_initialize_succes_ffff0000907e0e39,
                     s_dconfig_initenv_ffff0000907da784,0xde);
          return;
        }
        pcVar3 = s__s__d__cert_socid_mismatch_ffff000090802127;
        uVar4 = 0xd8;
      }
    }
    else {
      pcVar3 = s__s__d__ENV_SIG_is_wrong_ffff0000907f433f;
      uVar4 = 0xb8;
    }
  }
LAB_ffff00009072184c:
  FUN_ffff00009077de54(pcVar3,s_dconfig_initenv_ffff0000907da784,uVar4);
LAB_ffff000090721870:
  thunk_FUN_ffff0000907c2ad8(_DAT_ffff000090860600);
  _DAT_ffff000090860630 = 0;
  thunk_FUN_ffff0000907c2ad8(_DAT_ffff000090860628);
  _DAT_ffff000090860600 = 0;
  return;
}


