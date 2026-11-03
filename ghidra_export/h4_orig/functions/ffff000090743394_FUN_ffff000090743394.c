// ffff000090743394  size=448  FUN_ffff000090743394

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090743394(code *param_1)

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
  FUN_ffff00009077e11c
            (s__s__d___VCP___s____ffff0000907efd70,s_save_vcp_coredump_ffff0000907ee4b2,0x95,
             s_save_vcp_coredump_ffff0000907ee4b2);
  iVar3 = FUN_ffff000090743a0c();
  if (iVar3 == 0) {
    pcVar7 = s__VCP__vcp_is_not_supported__cann_ffff0000907eafe4;
  }
  else {
    iVar4 = FUN_ffff0000907444e8();
    iVar3 = _DAT_ffff000031800048;
    if (iVar4 == 0) {
      pcVar7 = s__VCP__vcp_power_disable__no_need_ffff0000907f1712;
    }
    else {
      if (_DAT_ffff000031800048 != 0) {
        FUN_ffff00009077e11c(s__VCP_vcp_loader_phy_0x_lx_ffff0000907dca60,_DAT_ffff000031800048);
        iVar4 = FUN_ffff00009077ba64
                          (DAT_ffff000090839928,s_me_vcp_reserved_ffff0000907cf9f2,0xc0200000,
                           &local_28,0xc,iVar3,0,0);
        if (iVar4 == 0) {
          FUN_ffff00009077e11c(s__VCP__vcp_loader_vir__p_ffff0000907fbb8a,local_28);
          lVar1 = local_28;
          uVar5 = FUN_ffff000090743984();
          FUN_ffff00009077ee50(lVar1,0,uVar5);
          uVar5 = FUN_ffff000090744154(local_28);
          lVar1 = local_28;
          uVar8 = FUN_ffff0000907439c8();
          lVar1 = lVar1 + (uVar8 & 0xffffffff);
          uVar6 = FUN_ffff000090743984();
          FUN_ffff00009077ee50(lVar1,0,uVar6);
          lVar2 = local_28;
          uVar6 = FUN_ffff000090743984();
          iVar4 = FUN_ffff000090743164(lVar2,lVar1,uVar5,uVar6);
          if (iVar4 - 0x400001U < 0xffc00002) {
            FUN_ffff00009077e11c(s__VCP__compressed_size_ERR__d_ffff0000907e309f,iVar4);
          }
          else {
            uVar9 = FUN_ffff00009077b334(lVar1);
            (*param_1)(uVar9,iVar4);
          }
          FUN_ffff00009077be30(DAT_ffff000090839928,iVar3);
        }
        else {
          FUN_ffff00009077e11c(s__VCP__map_dram_dump_pa_to_va_err_ffff0000907e64df,iVar4);
        }
        return;
      }
      pcVar7 = s__VCP__vcp_region_info___0__skip_c_ffff0000907d7c3d;
    }
  }
  FUN_ffff00009077e11c(pcVar7);
  return;
}


