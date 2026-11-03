
void FUN_ffff000090706ee0(long param_1)

{
  byte bVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  ulong uVar7;
  undefined8 uVar8;
  long lVar9;
  undefined *puVar10;
  undefined **ppuVar11;
  ulong uVar12;
  undefined4 local_6c;
  undefined1 local_68;
  uint local_64;
  
  local_68 = 0;
  local_6c = 0;
  if ((param_1 != 0) && (iVar2 = FUN_ffff0000907414e4(), iVar2 != 0)) {
    uVar12 = 0;
    puVar10 = &DAT_ffff000090816928;
    ppuVar11 = &PTR_FUN_ffff000090816a58;
    do {
      uVar3 = FUN_ffff00009071f368
                        (param_1,puVar10,(&PTR_s_mediatek_mt6379_gauge_1_ffff000090816ae0)[uVar12]);
      iVar2 = (int)uVar12;
      if (uVar3 < 2) {
        uVar8 = FUN_ffff00009071ee20();
        *(undefined8 *)(puVar10 + -0x48) = uVar8;
        iVar4 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_bat_gm30_ffff0000907df2bc);
        if ((iVar4 < 0) && (iVar4 = (*(code *)*ppuVar11)(param_1), iVar4 < 0)) {
          FUN_ffff00009077e11c
                    (s__s__d____s__battery__No_device_t_ffff0000907ef366,
                     s_mt6379_set_bat_data_ffff0000907ebde3,0x2fc,*(undefined8 *)(puVar10 + 0x10));
        }
        else {
          uVar3 = (*(uint *)(*(long *)(puVar10 + -0x48) + 4) & 0xff00ff00) >> 8 |
                  (*(uint *)(*(long *)(puVar10 + -0x48) + 4) & 0xff00ff) << 8;
          local_64 = uVar3 >> 0x10 | uVar3 << 0x10;
          iVar5 = FUN_ffff00009076e6d4(param_1,iVar4,s_shutdown_time_ffff0000907fe978,&local_64,4);
          if (iVar5 != 0) {
            FUN_ffff00009077e11c
                      (s__s__d___s__Failed_to_parse_BAT_d_ffff0000907f924b,
                       s_mt6379_set_bat_data_ffff0000907ebde3,0x2cf,
                       s_mt6379_set_bat_data_ffff0000907ebde3,iVar2 + 1,iVar5);
          }
          uVar3 = (*(uint *)(*(long *)(puVar10 + -0x48) + 8) & 0xff00ff00) >> 8 |
                  (*(uint *)(*(long *)(puVar10 + -0x48) + 8) & 0xff00ff) << 8;
          local_64 = uVar3 >> 0x10 | uVar3 << 0x10;
          iVar5 = FUN_ffff00009076e6d4(param_1,iVar4,s_fg_swocv_v_ffff0000907d099e,&local_64,4);
          if (iVar5 != 0) {
            FUN_ffff00009077e11c
                      (s__s__d___s__Failed_to_parse_BAT_d_ffff0000907d09a9,
                       s_mt6379_set_bat_data_ffff0000907ebde3,0x2d5,
                       s_mt6379_set_bat_data_ffff0000907ebde3,iVar2 + 1,iVar5);
          }
          uVar3 = (*(uint *)(*(long *)(puVar10 + -0x48) + 0xc) & 0xff00ff00) >> 8 |
                  (*(uint *)(*(long *)(puVar10 + -0x48) + 0xc) & 0xff00ff) << 8;
          local_64 = uVar3 >> 0x10 | uVar3 << 0x10;
          iVar5 = FUN_ffff00009076e6d4(param_1,iVar4,s_fg_swocv_i_ffff0000907e09b6,&local_64,4);
          if (iVar5 != 0) {
            FUN_ffff00009077e11c
                      (s__s__d___s__Failed_to_parse_BAT_d_ffff0000907e09c1,
                       s_mt6379_set_bat_data_ffff0000907ebde3,0x2db,
                       s_mt6379_set_bat_data_ffff0000907ebde3,iVar2 + 1,iVar5);
          }
          iVar2 = FUN_ffff000090721350();
          iVar5 = FUN_ffff000090721490();
          if (iVar5 == 0x2134) {
            FUN_ffff0000907213d8(&local_6c,5);
            iVar6 = FUN_ffff00009077f348(&local_6c,&DAT_ffff0000907ea5cf,4);
            bVar1 = (iVar6 != 0) << 1;
          }
          else if (iVar5 == 9000) {
            bVar1 = iVar2 != 0;
          }
          else {
            bVar1 = 0;
          }
          local_64 = (uint)bVar1 << 0x18;
          FUN_ffff00009076e6d4(param_1,iVar4,s_bat_type_ffff0000907e73ac,&local_64,4);
          lVar9 = *(long *)(puVar10 + -0x48);
          FUN_ffff00009077e11c
                    (s__s__d____s__battery__Set_shutdow_ffff0000907ebe1e,
                     s_mt6379_set_bat_data_ffff0000907ebde3,0x2fa,*(undefined8 *)(puVar10 + 0x10),
                     *(undefined4 *)(lVar9 + 4),*(undefined4 *)(lVar9 + 8),
                     *(undefined4 *)(lVar9 + 0xc),bVar1,iVar2,iVar5);
        }
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d___s__BAT_d_gauge_number_er_ffff0000907d72c8,
                   s_mt6379_set_bat_data_ffff0000907ebde3,0x2c0,
                   s_mt6379_set_bat_data_ffff0000907ebde3,iVar2 + 1);
      }
      uVar12 = uVar12 + 1;
      uVar7 = FUN_ffff0000907414e4();
      puVar10 = puVar10 + 0xb8;
      ppuVar11 = ppuVar11 + 9;
    } while (uVar12 < (uVar7 & 0xffffffff));
  }
  return;
}


