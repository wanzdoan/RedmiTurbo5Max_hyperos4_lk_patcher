// ffff000090712c24  size=564  FUN_ffff000090712c24

void FUN_ffff000090712c24(undefined8 param_1)

{
  bool bVar1;
  uint uVar2;
  long lVar3;
  int iVar4;
  uint *puVar5;
  long lVar6;
  char *pcVar7;
  ulong uVar8;
  long lVar9;
  long *plVar10;
  ulong uVar11;
  uint uVar12;
  ulong local_60;
  undefined4 local_54;
  
  local_54 = 0;
  iVar4 = FUN_ffff0000907695a8();
  if (iVar4 != 2) {
    pcVar7 = s_met_set_fdt_skip_if_user_load_wi_ffff0000907e5c0f;
LAB_ffff000090712d34:
    FUN_ffff00009077e11c(pcVar7);
    return;
  }
  iVar4 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_met_res_ram_ffff0000907f9410);
  if (iVar4 < 0) {
    pcVar7 = s_met_set_fdt_skip_if_met_res_ram_i_ffff0000907dc055;
    goto LAB_ffff000090712d34;
  }
  iVar4 = FUN_ffff00009076d02c(param_1,iVar4);
  if (-1 < iVar4) {
    uVar12 = 0;
    uVar11 = 0xffffffff;
    do {
      puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar4,&DAT_ffff0000907d587a,&local_54);
      if (puVar5 == (uint *)0x0) {
        pcVar7 = s_met_set_fdt_skip_if_res_ram_size_ffff0000907d8cef;
LAB_ffff000090712cb4:
        FUN_ffff00009077e11c(pcVar7);
      }
      else {
        uVar2 = *puVar5;
        pcVar7 = s_met_set_fdt_skip_if_res_ram_size_ffff0000907f4176;
        if (uVar2 == 0) goto LAB_ffff000090712cb4;
        if (1 < (int)uVar11) {
          FUN_ffff00009077e11c(s_met_set_fdt_skip_if_met_res_ram_t_ffff0000907f9425,3);
          return;
        }
        uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
        uVar11 = (long)(int)uVar11 + 1;
        uVar8 = (ulong)uVar12;
        uVar12 = (uVar2 >> 0x10 | uVar2 << 0x10) + uVar12;
        *(int *)(uVar11 * 0x10 + -0xffff6f7c07f8) = iVar4;
        *(ulong *)(uVar11 * 0x10 + -0xffff6f7c07f0) = uVar8;
      }
      iVar4 = FUN_ffff00009076d0fc(param_1,iVar4);
    } while (-1 < iVar4);
    if (uVar12 != 0) {
      lVar6 = FUN_ffff0000907198dc
                        (uVar12,0x10000,0x9fffffff,0,0,s_debug_met_res_ram_ffff0000907d3f5a);
      if (lVar6 != 0) {
        iVar4 = FUN_ffff000090732838(lVar6,uVar12);
        if (iVar4 != 0) {
          FUN_ffff00009077e11c(s_met_set_emi_mpu_ret__d_ffff0000907e8ddd,iVar4);
        }
        if ((int)uVar11 < 0) {
          return;
        }
        plVar10 = (long *)((uVar11 & 0xffffffff) * 0x10 + -0xffff6f7c07f0);
        lVar9 = (uVar11 & 0xffffffff) + 1;
        do {
          uVar11 = (*plVar10 + lVar6 & 0xff00ff00ff00ff00U) >> 8 |
                   (*plVar10 + lVar6 & 0xff00ff00ff00ffU) << 8;
          uVar11 = (uVar11 & 0xffff0000ffff0000) >> 0x10 | (uVar11 & 0xffff0000ffff) << 0x10;
          local_60 = uVar11 >> 0x20 | uVar11 << 0x20;
          iVar4 = FUN_ffff00009076e6d4
                            (param_1,(int)plVar10[-1],s_start_ffff0000907d3f6c,&local_60,8);
          plVar10 = plVar10 + -2;
          if (iVar4 < 0) {
            FUN_ffff00009077e11c(s_met_set_start_addr_fails_ffff0000907dc081);
          }
          lVar3 = lVar9 + -1;
          bVar1 = 0 < lVar9;
          lVar9 = lVar3;
        } while (lVar3 != 0 && bVar1);
        return;
      }
      pcVar7 = s_met_res_ram_allocation_failed_ffff0000907f9451;
      goto LAB_ffff000090712e14;
    }
  }
  pcVar7 = s_met_set_fdt_skip_if_total_res_ra_ffff0000907fb19c;
LAB_ffff000090712e14:
  FUN_ffff00009077e11c(pcVar7);
  return;
}


