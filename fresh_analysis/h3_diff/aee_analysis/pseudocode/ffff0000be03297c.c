
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be03297c(code *param_1)

{
  long lVar1;
  long lVar2;
  int iVar3;
  int iVar4;
  undefined4 uVar5;
  undefined4 uVar6;
  char *pcVar7;
  ulong uVar8;
  undefined8 uVar9;
  long local_28;
  
  local_28 = 0;
  FUN_ffff0000be05fd98
            (s__s__d___VCP___s____ffff0000be0b9e2d,s_save_vcp_coredump_ffff0000be0b8bc1,0x95,
             s_save_vcp_coredump_ffff0000be0b8bc1);
  iVar3 = FUN_ffff0000be032bc4();
  if (iVar3 == 0) {
    pcVar7 = s__VCP__vcp_is_not_supported__cann_ffff0000be0b6730;
  }
  else {
    iVar4 = FUN_ffff0000be0336a0();
    iVar3 = _DAT_ffff000031800048;
    if (iVar4 == 0) {
      pcVar7 = s__VCP__vcp_power_disable__no_need_ffff0000be0bb035;
    }
    else {
      if (_DAT_ffff000031800048 != 0) {
        FUN_ffff0000be05fd98(s__VCP_vcp_loader_phy_0x_lx_ffff0000be0ac977,_DAT_ffff000031800048);
        iVar4 = FUN_ffff0000be05d9a8
                          (PTR_DAT_ffff0000be0f5570,s_me_vcp_reserved_ffff0000be0a35f9,0xc0200000,
                           &local_28,0xc,iVar3,0,0);
        if (iVar4 == 0) {
          FUN_ffff0000be05fd98(s__VCP__vcp_loader_vir__p_ffff0000be0c1dcf,local_28);
          lVar1 = local_28;
          uVar5 = FUN_ffff0000be032b3c();
          FUN_ffff0000be060ae0(lVar1,0,uVar5);
          uVar5 = FUN_ffff0000be03330c(local_28);
          lVar1 = local_28;
          uVar8 = FUN_ffff0000be032b80();
          lVar1 = lVar1 + (uVar8 & 0xffffffff);
          uVar6 = FUN_ffff0000be032b3c();
          FUN_ffff0000be060ae0(lVar1,0,uVar6);
          lVar2 = local_28;
          uVar6 = FUN_ffff0000be032b3c();
          iVar4 = FUN_ffff0000be03274c(lVar2,lVar1,uVar5,uVar6);
          if (iVar4 - 0x400001U < 0xffc00002) {
            FUN_ffff0000be05fd98(s__VCP__compressed_size_ERR__d_ffff0000be0b0e07,iVar4);
          }
          else {
            uVar9 = FUN_ffff0000be05d228(lVar1);
            (*param_1)(uVar9,iVar4);
          }
          FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,iVar3);
        }
        else {
          FUN_ffff0000be05fd98(s__VCP__map_dram_dump_pa_to_va_err_ffff0000be0b30ae,iVar4);
        }
        return;
      }
      pcVar7 = s__VCP__vcp_region_info___0__skip_c_ffff0000be0a9184;
    }
  }
  FUN_ffff0000be05fd98(pcVar7);
  return;
}


