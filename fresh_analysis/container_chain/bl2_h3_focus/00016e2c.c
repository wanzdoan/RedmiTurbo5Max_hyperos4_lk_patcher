// File offset entry: 0x16e2c

int FUN_ffff0000b8016e2c(void)

{
  int iVar1;
  byte *pbVar2;
  long lVar3;
  byte *pbVar4;
  char *pcVar5;
  long *plVar6;
  long lVar7;
  
  pbVar2 = (byte *)FUN_ffff0000b80167c8();
  lVar3 = FUN_ffff0000b80167dc();
  pbVar4 = (byte *)FUN_ffff0000b80167f0();
  if (((pbVar2 == (byte *)0x0) || (lVar3 == 0)) || (pbVar4 == (byte *)0x0)) {
    FUN_ffff0000b804beb0(s__GZ_INIT__config_env_not_valid_ffff0000b8094243);
    iVar1 = -5;
  }
  else {
    if ((((*pbVar2 >> 1 & 1) != 0) || ((*pbVar4 & 1) != 0)) &&
       (iVar1 = FUN_ffff0000b801796c(), iVar1 != 0)) {
      FUN_ffff0000b804beb0(s__GZ_INIT__gz_remap_init__failed__ffff0000b809e43a,iVar1);
    }
    iVar1 = FUN_ffff0000b8017238(0xffffffff);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000b80174f4(0xffffffff);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000b8016854();
        if (iVar1 == 0) {
          iVar1 = FUN_ffff0000b8016894();
          if (((iVar1 == 0) && (iVar1 = FUN_ffff0000b80169a0(), iVar1 == 0)) &&
             (iVar1 = FUN_ffff0000b8016c08(), iVar1 == 0)) {
            plVar6 = (long *)FUN_ffff0000b8017214();
            if ((plVar6 == (long *)0x0) || (lVar3 = *plVar6, lVar3 == 0)) {
              FUN_ffff0000b804beb0(s__GZ_INIT__gz_mblock_info_get__fa_ffff0000b808b509,0);
            }
            else {
              if ((*pbVar2 >> 1 & 1) == 0) {
                lVar7 = 0;
              }
              else {
                lVar7 = FUN_ffff0000b8017710(1);
              }
              iVar1 = FUN_ffff0000b8016cc4(lVar7 + lVar3);
              if (iVar1 != 0) {
                pcVar5 = s__GZ_INIT__gz_config_push_jump_ad_ffff0000b809063d;
                goto LAB_ffff0000b8016f3c;
              }
            }
            iVar1 = 0;
            pcVar5 = s__GZ_INIT__init_success__gz_will_b_ffff0000b8087cb1;
            *pbVar2 = *pbVar2 & 0xfe;
            goto LAB_ffff0000b8016ec8;
          }
          pcVar5 = s__GZ_INIT__gz_config_param_update_ffff0000b808f8c1;
        }
        else {
          pcVar5 = s__GZ_INIT__gz_config_info_update__ffff0000b8099793;
        }
LAB_ffff0000b8016f3c:
        FUN_ffff0000b804beb0(pcVar5,iVar1);
      }
      else {
        FUN_ffff0000b804beb0(s__GZ_INIT__failed_while_loading_i_ffff0000b809b303);
      }
    }
    else {
      FUN_ffff0000b804beb0(s__GZ_INIT__failed_to_create_mbloc_ffff0000b808e9f0);
    }
  }
  FUN_ffff0000b8016804();
  FUN_ffff0000b80173f8(0xffffffff);
  pcVar5 = s__GZ_INIT__init_failed__gz_is_dis_ffff0000b80997c4;
LAB_ffff0000b8016ec8:
  FUN_ffff0000b804beb0(pcVar5);
  return iVar1;
}


