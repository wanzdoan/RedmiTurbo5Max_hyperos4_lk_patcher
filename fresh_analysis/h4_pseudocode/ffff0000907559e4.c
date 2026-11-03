
undefined8 FUN_ffff0000907559e4(undefined8 *param_1,int param_2)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  ulong uVar4;
  long lVar5;
  undefined8 uVar6;
  ulong uVar7;
  
  if (param_1 == (undefined8 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0x282,
               s_dp_handle____NULL_ffff0000907e81a2);
  }
  puVar3 = (uint *)FUN_ffff0000907593e8(*(undefined4 *)(param_1 + 0x1c));
  uVar2 = FUN_ffff0000907590e8(*(undefined4 *)(param_1 + 0x1c));
  uVar7 = (ulong)uVar2;
  if (param_2 == 0) {
    uVar6 = 0;
  }
  else {
    uVar6 = *param_1;
  }
  FUN_ffff00009075a4dc(*(undefined4 *)(param_1 + 1),uVar6);
  uVar4 = FUN_ffff0000907590bc(*(undefined4 *)(param_1 + 0x1c));
  if ((uVar4 & 1) != 0) {
    FUN_ffff00009075a534(*(undefined4 *)(param_1 + 1),uVar6);
  }
  FUN_ffff00009075534c(*(undefined4 *)(param_1 + 0x1c),uVar6);
  puVar1 = PTR_DAT_ffff0000908399c8;
  if (0 < (int)uVar2) {
    do {
      uVar4 = (ulong)*puVar3;
      lVar5 = *(long *)(puVar1 + uVar4 * 8);
      if (lVar5 != 0) {
        if (*(code **)(lVar5 + 0x10) != (code *)0x0) {
          (**(code **)(lVar5 + 0x10))(uVar4,uVar6);
          lVar5 = *(long *)(puVar1 + uVar4 * 8);
        }
        if (*(code **)(lVar5 + 0x98) != (code *)0x0) {
          (**(code **)(lVar5 + 0x98))(uVar4,0);
        }
      }
      uVar7 = uVar7 - 1;
      puVar3 = puVar3 + 1;
    } while (uVar7 != 0);
  }
  *(undefined4 *)((long)param_1 + 0xc) = 0;
  FUN_ffff000090758ef4();
  if ((DAT_ffff00009087a998 & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087a9a0,0,0xc50);
    DAT_ffff00009087a9a4 = 0xf;
    DAT_ffff00009087a998 = 1;
  }
  if ((((DAT_ffff00009087a9a8 != 0) &&
       ((DAT_ffff00009087b5d0 == 0 || (*(int *)(DAT_ffff00009087b5d0 + 0xc) == 0)))) &&
      ((DAT_ffff00009087b5d8 == 0 || (*(int *)(DAT_ffff00009087b5d8 + 0xc) == 0)))) &&
     (((DAT_ffff00009087b5e0 == 0 || (*(int *)(DAT_ffff00009087b5e0 + 0xc) == 0)) &&
      ((DAT_ffff00009087b5e8 == 0 || (*(int *)(DAT_ffff00009087b5e8 + 0xc) == 0)))))) {
    DAT_ffff00009087a9a8 = 0;
    FUN_ffff00009075a6f8();
  }
  return 0;
}


