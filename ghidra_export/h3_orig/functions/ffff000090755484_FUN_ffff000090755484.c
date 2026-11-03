// ffff000090755484  size=712  FUN_ffff000090755484

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090755484(undefined8 *param_1,int param_2,undefined8 param_3,ulong param_4)

{
  undefined4 uVar1;
  long lVar2;
  uint uVar3;
  ulong uVar4;
  uint *puVar5;
  long lVar6;
  undefined8 uVar7;
  ulong uVar8;
  ulong uVar9;
  undefined4 local_64;
  
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff000090759150(*(undefined4 *)(param_1 + 0x1c));
    FUN_ffff000090758e50(*(undefined4 *)(param_1 + 0x1c));
    if (param_2 == 0) {
      uVar7 = 0;
    }
    else {
      uVar7 = *param_1;
    }
    if ((DAT_ffff00009087a900 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087a908,0,0xc50);
      _DAT_ffff00009087a90c = 0xf;
      DAT_ffff00009087a900 = 1;
    }
    if (_DAT_ffff00009087a910 == 0) {
      _DAT_ffff00009087a910 = 1;
      FUN_ffff00009075a394();
    }
    FUN_ffff00009075a48c();
    FUN_ffff000090758c90();
    if ((param_4 & 1) == 0) {
      FUN_ffff000090759d00
                (*(undefined4 *)(param_1 + 1),*(undefined4 *)(param_1 + 0x1c),
                 *(undefined4 *)(param_1 + 2),uVar7);
    }
    FUN_ffff0000907551b0(*(undefined4 *)(param_1 + 0x1c),uVar7);
    FUN_ffff000090759c5c(uVar7,param_3);
    uVar1 = *(undefined4 *)(param_1 + 0x1c);
    uVar4 = FUN_ffff000090758e24(uVar1);
    if ((uVar4 & 1) == 0) {
      local_64 = 0x14;
    }
    else {
      local_64 = FUN_ffff000090758e38(uVar1);
    }
    puVar5 = (uint *)FUN_ffff000090759150(uVar1);
    uVar3 = FUN_ffff000090758e50(uVar1);
    lVar2 = DAT_ffff0000908399a8;
    if (0 < (int)uVar3) {
      uVar9 = (ulong)uVar3;
      do {
        uVar3 = *puVar5;
        uVar8 = (ulong)uVar3;
        lVar6 = *(long *)(lVar2 + uVar8 * 8);
        if (lVar6 != 0) {
          if (*(code **)(lVar6 + 8) != (code *)0x0) {
            (**(code **)(lVar6 + 8))(uVar8,uVar7);
            lVar6 = *(long *)(lVar2 + uVar8 * 8);
          }
          if (*(code **)(lVar6 + 0x98) != (code *)0x0) {
            (**(code **)(lVar6 + 0x98))(uVar8,FUN_ffff0000907553fc);
          }
        }
        if (uVar3 == 0xb7) {
          _DAT_ffff000032400b30 = _DAT_ffff000032400b30 & 0xfffffffc;
        }
        else if (uVar3 == 0xb6) {
          _DAT_ffff000032000b34 = _DAT_ffff000032000b34 & 0xfffffffc;
        }
        uVar9 = uVar9 - 1;
        puVar5 = puVar5 + 1;
      } while (uVar9 != 0);
    }
    if ((uVar4 & 1) != 0) {
      puVar5 = (uint *)FUN_ffff000090759150(local_64);
      uVar3 = FUN_ffff000090758e50(local_64);
      lVar2 = DAT_ffff0000908399a8;
      if (0 < (int)uVar3) {
        uVar4 = (ulong)uVar3;
        do {
          uVar3 = *puVar5;
          uVar9 = (ulong)uVar3;
          lVar6 = *(long *)(lVar2 + uVar9 * 8);
          if (lVar6 != 0) {
            if (*(code **)(lVar6 + 8) != (code *)0x0) {
              (**(code **)(lVar6 + 8))(uVar9,uVar7);
              lVar6 = *(long *)(lVar2 + uVar9 * 8);
            }
            if (*(code **)(lVar6 + 0x98) != (code *)0x0) {
              (**(code **)(lVar6 + 0x98))(uVar9,FUN_ffff0000907553fc);
            }
          }
          if (uVar3 == 0xb6) {
            _DAT_ffff000032000b34 = _DAT_ffff000032000b34 & 0xfffffffc;
          }
          else if (uVar3 == 0xb7) {
            _DAT_ffff000032400b30 = _DAT_ffff000032400b30 & 0xfffffffc;
          }
          uVar4 = uVar4 - 1;
          puVar5 = puVar5 + 1;
        } while (uVar4 != 0);
      }
    }
    *(undefined4 *)((long)param_1 + 0xc) = 1;
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0x230,
             s_dp_handle____NULL_ffff0000907e805e);
}


