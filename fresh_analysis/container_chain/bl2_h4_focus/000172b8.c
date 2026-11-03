// File offset entry: 0x172b8

int FUN_ffff0000b80172b8(void)

{
  int iVar1;
  byte *pbVar2;
  long lVar3;
  byte *pbVar4;
  char *pcVar5;
  long *plVar6;
  long lVar7;
  
  pbVar2 = (byte *)FUN_ffff0000b8016c54();
  lVar3 = FUN_ffff0000b8016c68();
  pbVar4 = (byte *)FUN_ffff0000b8016c7c();
  if (((pbVar2 == (byte *)0x0) || (lVar3 == 0)) || (pbVar4 == (byte *)0x0)) {
    FUN_ffff0000b804c33c(s__GZ_INIT__config_env_not_valid_ffff0000b809533e);
    iVar1 = -5;
  }
  else {
    if ((((*pbVar2 >> 1 & 1) != 0) || ((*pbVar4 & 1) != 0)) &&
       (iVar1 = FUN_ffff0000b8017df8(), iVar1 != 0)) {
      FUN_ffff0000b804c33c(s__GZ_INIT__gz_remap_init__failed__ffff0000b809f54d,iVar1);
    }
    iVar1 = FUN_ffff0000b80176c4(0xffffffff);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000b8017980(0xffffffff);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000b8016ce0();
        if (iVar1 == 0) {
          iVar1 = FUN_ffff0000b8016d20();
          if (((iVar1 == 0) && (iVar1 = FUN_ffff0000b8016e2c(), iVar1 == 0)) &&
             (iVar1 = FUN_ffff0000b8017094(), iVar1 == 0)) {
            plVar6 = (long *)FUN_ffff0000b80176a0();
            if ((plVar6 == (long *)0x0) || (lVar3 = *plVar6, lVar3 == 0)) {
              FUN_ffff0000b804c33c(s__GZ_INIT__gz_mblock_info_get__fa_ffff0000b808c54f,0);
            }
            else {
              if ((*pbVar2 >> 1 & 1) == 0) {
                lVar7 = 0;
              }
              else {
                lVar7 = FUN_ffff0000b8017b9c(1);
              }
              iVar1 = FUN_ffff0000b8017150(lVar7 + lVar3);
              if (iVar1 != 0) {
                pcVar5 = s__GZ_INIT__gz_config_push_jump_ad_ffff0000b80916c6;
                goto LAB_ffff0000b80173c8;
              }
            }
            iVar1 = 0;
            pcVar5 = s__GZ_INIT__init_success__gz_will_b_ffff0000b8088ce6;
            *pbVar2 = *pbVar2 & 0xfe;
            goto LAB_ffff0000b8017354;
          }
          pcVar5 = s__GZ_INIT__gz_config_param_update_ffff0000b809094a;
        }
        else {
          pcVar5 = s__GZ_INIT__gz_config_info_update__ffff0000b809a876;
        }
LAB_ffff0000b80173c8:
        FUN_ffff0000b804c33c(pcVar5,iVar1);
      }
      else {
        FUN_ffff0000b804c33c(s__GZ_INIT__failed_while_loading_i_ffff0000b809c402);
      }
    }
    else {
      FUN_ffff0000b804c33c(s__GZ_INIT__failed_to_create_mbloc_ffff0000b808fa50);
    }
  }
  FUN_ffff0000b8016c90();
  FUN_ffff0000b8017884(0xffffffff);
  pcVar5 = s__GZ_INIT__init_failed__gz_is_dis_ffff0000b809a8a7;
LAB_ffff0000b8017354:
  FUN_ffff0000b804c33c(pcVar5);
  return iVar1;
}


