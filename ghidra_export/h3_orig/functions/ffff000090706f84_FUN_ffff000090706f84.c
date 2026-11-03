// ffff000090706f84  size=680  FUN_ffff000090706f84

void FUN_ffff000090706f84(long param_1)

{
  byte bVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  ulong uVar6;
  undefined8 uVar7;
  long lVar8;
  undefined *puVar9;
  undefined **ppuVar10;
  ulong uVar11;
  uint local_64;
  
  if ((param_1 != 0) && (iVar2 = FUN_ffff00009074124c(), iVar2 != 0)) {
    uVar11 = 0;
    puVar9 = &DAT_ffff000090816918;
    ppuVar10 = &PTR_FUN_ffff000090816a48;
    do {
      uVar3 = FUN_ffff00009071f1e0
                        (param_1,puVar9,(&PTR_s_mediatek_mt6379_gauge_1_ffff000090816ad0)[uVar11]);
      iVar2 = (int)uVar11;
      if (uVar3 < 2) {
        uVar7 = FUN_ffff00009071ec98();
        *(undefined8 *)(puVar9 + -0x48) = uVar7;
        iVar4 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_bat_gm30_ffff0000907df219);
        if ((iVar4 < 0) && (iVar4 = (*(code *)*ppuVar10)(param_1), iVar4 < 0)) {
          FUN_ffff00009077de54
                    (s__s__d____s__battery__No_device_t_ffff0000907ef1f5,
                     s_mt6379_set_bat_data_ffff0000907ebc60,0x2f6,*(undefined8 *)(puVar9 + 0x10));
        }
        else {
          uVar3 = (*(uint *)(*(long *)(puVar9 + -0x48) + 4) & 0xff00ff00) >> 8 |
                  (*(uint *)(*(long *)(puVar9 + -0x48) + 4) & 0xff00ff) << 8;
          local_64 = uVar3 >> 0x10 | uVar3 << 0x10;
          iVar5 = FUN_ffff00009076e40c(param_1,iVar4,s_shutdown_time_ffff0000907fe7d0,&local_64,4);
          if (iVar5 != 0) {
            FUN_ffff00009077de54
                      (s__s__d___s__Failed_to_parse_BAT_d_ffff0000907f90af,
                       s_mt6379_set_bat_data_ffff0000907ebc60,0x2ce,
                       s_mt6379_set_bat_data_ffff0000907ebc60,iVar2 + 1,iVar5);
          }
          uVar3 = (*(uint *)(*(long *)(puVar9 + -0x48) + 8) & 0xff00ff00) >> 8 |
                  (*(uint *)(*(long *)(puVar9 + -0x48) + 8) & 0xff00ff) << 8;
          local_64 = uVar3 >> 0x10 | uVar3 << 0x10;
          iVar5 = FUN_ffff00009076e40c(param_1,iVar4,s_fg_swocv_v_ffff0000907d0987,&local_64,4);
          if (iVar5 != 0) {
            FUN_ffff00009077de54
                      (s__s__d___s__Failed_to_parse_BAT_d_ffff0000907d0992,
                       s_mt6379_set_bat_data_ffff0000907ebc60,0x2d4,
                       s_mt6379_set_bat_data_ffff0000907ebc60,iVar2 + 1,iVar5);
          }
          uVar3 = (*(uint *)(*(long *)(puVar9 + -0x48) + 0xc) & 0xff00ff00) >> 8 |
                  (*(uint *)(*(long *)(puVar9 + -0x48) + 0xc) & 0xff00ff) << 8;
          local_64 = uVar3 >> 0x10 | uVar3 << 0x10;
          iVar5 = FUN_ffff00009076e40c(param_1,iVar4,s_fg_swocv_i_ffff0000907e0913,&local_64,4);
          if (iVar5 != 0) {
            FUN_ffff00009077de54
                      (s__s__d___s__Failed_to_parse_BAT_d_ffff0000907e091e,
                       s_mt6379_set_bat_data_ffff0000907ebc60,0x2da,
                       s_mt6379_set_bat_data_ffff0000907ebc60,iVar2 + 1,iVar5);
          }
          iVar2 = FUN_ffff0000907211c8();
          iVar5 = FUN_ffff000090721250();
          if (iVar5 == 0x2134) {
            bVar1 = 2;
          }
          else if (iVar5 == 9000) {
            bVar1 = iVar2 != 0;
          }
          else {
            bVar1 = 0;
          }
          local_64 = (uint)bVar1 << 0x18;
          FUN_ffff00009076e40c(param_1,iVar4,s_bat_type_ffff0000907e7268,&local_64,4);
          lVar8 = *(long *)(puVar9 + -0x48);
          FUN_ffff00009077de54
                    (s__s__d____s__battery__Set_shutdow_ffff0000907ebc9b,
                     s_mt6379_set_bat_data_ffff0000907ebc60,0x2f4,*(undefined8 *)(puVar9 + 0x10),
                     *(undefined4 *)(lVar8 + 4),*(undefined4 *)(lVar8 + 8),
                     *(undefined4 *)(lVar8 + 0xc),bVar1,iVar2,iVar5);
        }
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d___s__BAT_d_gauge_number_er_ffff0000907d7232,
                   s_mt6379_set_bat_data_ffff0000907ebc60,0x2bf,
                   s_mt6379_set_bat_data_ffff0000907ebc60,iVar2 + 1);
      }
      uVar11 = uVar11 + 1;
      uVar6 = FUN_ffff00009074124c();
      puVar9 = puVar9 + 0xb8;
      ppuVar10 = ppuVar10 + 9;
    } while (uVar11 < (uVar6 & 0xffffffff));
  }
  return;
}


