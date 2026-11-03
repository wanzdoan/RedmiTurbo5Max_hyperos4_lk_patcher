
int FUN_ffff0000b8004bbc(void)

{
  long lVar1;
  int iVar2;
  char *pcVar3;
  undefined8 uVar4;
  uint local_14;
  
  DAT_ffff0000b810c070 = FUN_ffff0000b8020e20(s_mt6379_ffff0000b8094000);
  if (DAT_ffff0000b810c070 == 0) {
    return -7;
  }
  local_14 = 0;
  iVar2 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0,&local_14);
  lVar1 = DAT_ffff0000b810c070;
  if (iVar2 != 0) {
    return iVar2;
  }
  if ((local_14 & 0xf0) != 0x70) {
    FUN_ffff0000b804c33c
              (s__s__d__vendor_id_0x_X_is_incorre_ffff0000b809a59b,
               s_mt6379_check_devid_ffff0000b80906ab,0x213);
    return -7;
  }
  local_14 = 0;
  iVar2 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x10e,&local_14);
  if (-1 < iVar2) {
    local_14 = local_14 & 0xffffff80 | 0x2d;
    iVar2 = FUN_ffff0000b80211d4(lVar1,0x10e);
    lVar1 = DAT_ffff0000b810c070;
    if (iVar2 == 0) {
      local_14 = 0;
      iVar2 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x109,&local_14);
      if (-1 < iVar2) {
        local_14 = local_14 & 0xffffff00 | 0x10;
        iVar2 = FUN_ffff0000b80211d4(lVar1,0x109);
        lVar1 = DAT_ffff0000b810c070;
        if (iVar2 == 0) {
          local_14 = 0;
          DAT_ffff0000b80ad618 = 500;
          iVar2 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x10a,&local_14);
          if (-1 < iVar2) {
            local_14 = local_14 & 0xffffff7f;
            iVar2 = FUN_ffff0000b80211d4(lVar1,0x10a);
            lVar1 = DAT_ffff0000b810c070;
            if (iVar2 == 0) {
              local_14 = 0;
              iVar2 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x10b,&local_14);
              if (-1 < iVar2) {
                local_14 = local_14 & 0xffffff00 | 6;
                iVar2 = FUN_ffff0000b80211d4(lVar1,0x10b);
                lVar1 = DAT_ffff0000b810c070;
                if (iVar2 == 0) {
                  local_14 = 0;
                  iVar2 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x117,&local_14);
                  if (-1 < iVar2) {
                    local_14 = local_14 & 0xffffffe0 | 2;
                    iVar2 = FUN_ffff0000b80211d4(lVar1,0x117);
                    if (iVar2 == 0) {
                      iVar2 = FUN_ffff0000b8004fd8(500);
                      if (iVar2 == 0) {
                        return 0;
                      }
                      pcVar3 = s__s__d__Failed_to_set_ichg_ffff0000b808987d;
                      uVar4 = 0x2cf;
                      goto LAB_ffff0000b8004dc0;
                    }
                  }
                  pcVar3 = s__s__d__Failed_to_set_iprec_ffff0000b809ffe5;
                  uVar4 = 0x2c9;
                  goto LAB_ffff0000b8004dc0;
                }
              }
              pcVar3 = s__s__d__Failed_to_set_mivr_ffff0000b809e19c;
              uVar4 = 0x2c3;
              goto LAB_ffff0000b8004dc0;
            }
          }
          pcVar3 = s__s__d__Failed_to_disable_ilim_ffff0000b809224d;
          uVar4 = 0x2bd;
          goto LAB_ffff0000b8004dc0;
        }
      }
      pcVar3 = s__s__d__Failed_to_set_aicr_ffff0000b80989dd;
      uVar4 = 0x2b7;
      goto LAB_ffff0000b8004dc0;
    }
  }
  pcVar3 = s__s__d__Failed_to_set_cv_ffff0000b809e183;
  uVar4 = 0x2b1;
LAB_ffff0000b8004dc0:
  FUN_ffff0000b804c33c(pcVar3,s_mt6379_chg_init_setting_ffff0000b8086e09,uVar4);
  FUN_ffff0000b804c33c
            (s__s__d__Failed_to_init_setting_ffff0000b809222e,
             s__mt6379_charger_init_ffff0000b80989c8,0x359);
  return iVar2;
}


