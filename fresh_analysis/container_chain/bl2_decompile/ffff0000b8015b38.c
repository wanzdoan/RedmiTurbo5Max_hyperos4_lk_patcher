
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8015b38(long param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  uint uVar5;
  long lVar6;
  int local_24;
  
  local_24 = 0;
  if (param_1 == 0) {
    FUN_ffff0000b804c33c
              (s___s_dvfs_no_device_tree__disable_ffff0000b809f4de,s_dvfs_init_ffff0000b8097e31);
    uVar5 = 0;
  }
  else {
    iVar2 = FUN_ffff0000b80483cc(param_1,0xffffffff,s_mediatek_cpufreq_hw_ffff0000b809d5c9);
    if (iVar2 < 0) {
      FUN_ffff0000b804c33c
                (s___s_dvfs_not_found_dvfs_node__di_ffff0000b809c3c5,s_dvfs_init_ffff0000b8097e31);
      uVar5 = 0;
    }
    else {
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_dvfs_config_ffff0000b808dce7,&local_24);
      if (lVar3 == 0) {
        uVar5 = 1;
      }
      else {
        lVar6 = (long)local_24;
        iVar2 = FUN_ffff0000b804d4c4(lVar3,s_dvfs_disable_ffff0000b808ea15,lVar6);
        if (iVar2 == 0) {
          uVar5 = 0x10;
        }
        else {
          iVar2 = FUN_ffff0000b804d4c4(lVar3,s_aging_ffff0000b809d5dd,lVar6);
          if (iVar2 == 0) {
            uVar5 = 0x41;
          }
          else {
            iVar2 = FUN_ffff0000b804d4c4(lVar3,s_mcl50_ffff0000b808a891,lVar6);
            if (iVar2 == 0) {
              uVar5 = 0x21;
            }
            else {
              iVar2 = FUN_ffff0000b804d4c4(lVar3,s_eem_disable_ffff0000b808b638,lVar6);
              uVar5 = 0x11;
              if (iVar2 != 0) {
                uVar5 = 1;
              }
            }
          }
        }
      }
    }
  }
  uVar4 = FUN_ffff0000b8010dfc();
  uVar1 = uVar5 | 0x1000;
  if ((uVar4 & 1) == 0) {
    uVar1 = uVar5;
  }
  iVar2 = FUN_ffff0000b803ffb8();
  FUN_ffff0000b804c33c(s__cpufreq__segment___0x_x_ffff0000b8088cb0,iVar2);
  _DAT_ffff00000011bc0c = uVar1 + iVar2 * 0x100;
  _DAT_ffff00000011bc08 = 0x55aa55aa;
  FUN_ffff0000b80075ac(0xffff00000011bc00,0x40);
  return;
}


