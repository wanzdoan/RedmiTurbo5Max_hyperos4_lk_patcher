// ffff0000907430fc  size=448  FUN_ffff0000907430fc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907430fc(code *param_1)

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
  FUN_ffff00009077de54
            (s__s__d___VCP___s____ffff0000907efbe8,s_save_vcp_coredump_ffff0000907ee32a,0x95,
             s_save_vcp_coredump_ffff0000907ee32a);
  iVar3 = FUN_ffff000090743774();
  if (iVar3 == 0) {
    pcVar7 = s__VCP__vcp_is_not_supported__cann_ffff0000907eae4a;
  }
  else {
    iVar4 = FUN_ffff000090744250();
    iVar3 = _DAT_ffff000031800048;
    if (iVar4 == 0) {
      pcVar7 = s__VCP__vcp_power_disable__no_need_ffff0000907f158a;
    }
    else {
      if (_DAT_ffff000031800048 != 0) {
        FUN_ffff00009077de54(s__VCP_vcp_loader_phy_0x_lx_ffff0000907dc973,_DAT_ffff000031800048);
        iVar4 = FUN_ffff00009077b79c
                          (DAT_ffff000090839908,s_me_vcp_reserved_ffff0000907cf9d3,0xc0200000,
                           &local_28,0xc,iVar3,0,0);
        if (iVar4 == 0) {
          FUN_ffff00009077de54(s__VCP__vcp_loader_vir__p_ffff0000907fb9ee,local_28);
          lVar1 = local_28;
          uVar5 = FUN_ffff0000907436ec();
          FUN_ffff00009077eb88(lVar1,0,uVar5);
          uVar5 = FUN_ffff000090743ebc(local_28);
          lVar1 = local_28;
          uVar8 = FUN_ffff000090743730();
          lVar1 = lVar1 + (uVar8 & 0xffffffff);
          uVar6 = FUN_ffff0000907436ec();
          FUN_ffff00009077eb88(lVar1,0,uVar6);
          lVar2 = local_28;
          uVar6 = FUN_ffff0000907436ec();
          iVar4 = FUN_ffff000090742ecc(lVar2,lVar1,uVar5,uVar6);
          if (iVar4 - 0x400001U < 0xffc00002) {
            FUN_ffff00009077de54(s__VCP__compressed_size_ERR__d_ffff0000907e2fb7,iVar4);
          }
          else {
            uVar9 = FUN_ffff00009077b06c(lVar1);
            (*param_1)(uVar9,iVar4);
          }
          FUN_ffff00009077bb68(DAT_ffff000090839908,iVar3);
        }
        else {
          FUN_ffff00009077de54(s__VCP__map_dram_dump_pa_to_va_err_ffff0000907e63b4,iVar4);
        }
        return;
      }
      pcVar7 = s__VCP__vcp_region_info___0__skip_c_ffff0000907d7ba7;
    }
  }
  FUN_ffff00009077de54(pcVar7);
  return;
}


