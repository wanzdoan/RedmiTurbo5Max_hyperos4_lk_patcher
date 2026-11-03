
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be040950(undefined8 *param_1,int param_2,undefined8 param_3,ulong param_4)

{
  undefined4 uVar1;
  undefined *puVar2;
  uint uVar3;
  ulong uVar4;
  uint *puVar5;
  long lVar6;
  undefined8 uVar7;
  ulong uVar8;
  ulong uVar9;
  undefined4 local_64;
  
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff0000be0443c4(*(undefined4 *)(param_1 + 0x1c));
    FUN_ffff0000be0440c4(*(undefined4 *)(param_1 + 0x1c));
    if (param_2 == 0) {
      uVar7 = 0;
    }
    else {
      uVar7 = *param_1;
    }
    if ((DAT_ffff0000be124ad8 & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be124ae0,0,0xc50);
      DAT_ffff0000be124ae4 = 0xf;
      DAT_ffff0000be124ad8 = 1;
    }
    if (DAT_ffff0000be124ae8 == 0) {
      DAT_ffff0000be124ae8 = 1;
      FUN_ffff0000be045608();
    }
    FUN_ffff0000be045700();
    FUN_ffff0000be043f04();
    if ((param_4 & 1) == 0) {
      FUN_ffff0000be044f74
                (*(undefined4 *)(param_1 + 1),*(undefined4 *)(param_1 + 0x1c),
                 *(undefined4 *)(param_1 + 2),uVar7);
    }
    FUN_ffff0000be04067c(*(undefined4 *)(param_1 + 0x1c),uVar7);
    FUN_ffff0000be044ed0(uVar7,param_3);
    uVar1 = *(undefined4 *)(param_1 + 0x1c);
    uVar4 = FUN_ffff0000be044098(uVar1);
    if ((uVar4 & 1) == 0) {
      local_64 = 0x14;
    }
    else {
      local_64 = FUN_ffff0000be0440ac(uVar1);
    }
    puVar5 = (uint *)FUN_ffff0000be0443c4(uVar1);
    uVar3 = FUN_ffff0000be0440c4(uVar1);
    puVar2 = PTR_DAT_ffff0000be0f5660;
    if (0 < (int)uVar3) {
      uVar9 = (ulong)uVar3;
      do {
        uVar3 = *puVar5;
        uVar8 = (ulong)uVar3;
        lVar6 = *(long *)(puVar2 + uVar8 * 8);
        if (lVar6 != 0) {
          if (*(code **)(lVar6 + 8) != (code *)0x0) {
            (**(code **)(lVar6 + 8))(uVar8,uVar7);
            lVar6 = *(long *)(puVar2 + uVar8 * 8);
          }
          if (*(code **)(lVar6 + 0x98) != (code *)0x0) {
            (**(code **)(lVar6 + 0x98))(uVar8,FUN_ffff0000be0408c8);
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
      puVar5 = (uint *)FUN_ffff0000be0443c4(local_64);
      uVar3 = FUN_ffff0000be0440c4(local_64);
      puVar2 = PTR_DAT_ffff0000be0f5660;
      if (0 < (int)uVar3) {
        uVar4 = (ulong)uVar3;
        do {
          uVar3 = *puVar5;
          uVar9 = (ulong)uVar3;
          lVar6 = *(long *)(puVar2 + uVar9 * 8);
          if (lVar6 != 0) {
            if (*(code **)(lVar6 + 8) != (code *)0x0) {
              (**(code **)(lVar6 + 8))(uVar9,uVar7);
              lVar6 = *(long *)(puVar2 + uVar9 * 8);
            }
            if (*(code **)(lVar6 + 0x98) != (code *)0x0) {
              (**(code **)(lVar6 + 0x98))(uVar9,FUN_ffff0000be0408c8);
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
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x230,
             s_dp_handle____NULL_ffff0000be0b463c);
}


