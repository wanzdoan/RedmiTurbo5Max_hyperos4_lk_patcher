
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8029ae4(code *param_1)

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
  FUN_ffff0000b804c33c
            (s__s__d___VCP___s____ffff0000b809733e,s_save_vcp_coredump_ffff0000b809652f,0x95,
             s_save_vcp_coredump_ffff0000b809652f);
  iVar3 = FUN_ffff0000b8029d2c();
  if (iVar3 == 0) {
    pcVar7 = s__VCP__vcp_is_not_supported__cann_ffff0000b8094705;
  }
  else {
    iVar4 = FUN_ffff0000b802a4d0();
    iVar3 = _DAT_ffff000031800048;
    if (iVar4 == 0) {
      pcVar7 = s__VCP__vcp_power_disable__no_need_ffff0000b80981db;
    }
    else {
      if (_DAT_ffff000031800048 != 0) {
        FUN_ffff0000b804c33c(s__VCP_vcp_loader_phy_0x_lx_ffff0000b808c7ea,_DAT_ffff000031800048);
        iVar4 = FUN_ffff0000b804a5f8
                          (PTR_DAT_ffff0000b81086d8,s_me_vcp_reserved_ffff0000b80854e8,0xc0200000,
                           &local_28,0xc,iVar3,0,0);
        if (iVar4 == 0) {
          FUN_ffff0000b804c33c(s__VCP__vcp_loader_vir__p_ffff0000b809d936,local_28);
          lVar1 = local_28;
          uVar5 = FUN_ffff0000b8029ca4();
          FUN_ffff0000b804d070(lVar1,0,uVar5);
          uVar5 = FUN_ffff0000b802a13c(local_28);
          lVar1 = local_28;
          uVar8 = FUN_ffff0000b8029ce8();
          lVar1 = lVar1 + (uVar8 & 0xffffffff);
          uVar6 = FUN_ffff0000b8029ca4();
          FUN_ffff0000b804d070(lVar1,0,uVar6);
          lVar2 = local_28;
          uVar6 = FUN_ffff0000b8029ca4();
          iVar4 = FUN_ffff0000b80298b4(lVar2,lVar1,uVar5,uVar6);
          if (iVar4 - 0x400001U < 0xffc00002) {
            FUN_ffff0000b804c33c(s__VCP__compressed_size_ERR__d_ffff0000b808fde3,iVar4);
          }
          else {
            uVar9 = FUN_ffff0000b8049ec8(lVar1);
            (*param_1)(uVar9,iVar4);
          }
          FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,iVar3);
        }
        else {
          FUN_ffff0000b804c33c(s__VCP__map_dram_dump_pa_to_va_err_ffff0000b8091a18,iVar4);
        }
        return;
      }
      pcVar7 = s__VCP__vcp_region_info___0__skip_c_ffff0000b8089cd0;
    }
  }
  FUN_ffff0000b804c33c(pcVar7);
  return;
}


