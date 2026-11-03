// ffff0000907563bc  size=336  FUN_ffff0000907563bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907563bc(undefined8 *param_1,int param_2)

{
  long lVar1;
  uint uVar2;
  uint *puVar3;
  undefined8 uVar4;
  long lVar5;
  code *pcVar6;
  ulong uVar7;
  
  if (param_1 != (undefined8 *)0x0) {
    puVar3 = (uint *)FUN_ffff0000907593e8(*(undefined4 *)(param_1 + 0x1c));
    uVar2 = FUN_ffff0000907590e8(*(undefined4 *)(param_1 + 0x1c));
    lVar1 = DAT_ffff0000908399c8;
    if (0 < (int)uVar2) {
      uVar7 = (ulong)uVar2;
      do {
        lVar5 = *(long *)(lVar1 + (ulong)*puVar3 * 8);
        if ((lVar5 != 0) && (pcVar6 = *(code **)(lVar5 + 0x58), pcVar6 != (code *)0x0)) {
          if (param_2 == 0) {
            uVar4 = 0;
          }
          else {
            uVar4 = *param_1;
          }
          (*pcVar6)((ulong)*puVar3,uVar4);
        }
        uVar7 = uVar7 - 1;
        puVar3 = puVar3 + 1;
      } while (uVar7 != 0);
    }
    *(undefined4 *)((long)param_1 + 0xc) = 0;
    if ((DAT_ffff00009087a998 & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087a9a0,0,0xc50);
      _DAT_ffff00009087a9a4 = 0xf;
      DAT_ffff00009087a998 = 1;
    }
    if ((((_DAT_ffff00009087a9a8 != 0) &&
         ((_DAT_ffff00009087b5d0 == 0 || (*(int *)(_DAT_ffff00009087b5d0 + 0xc) == 0)))) &&
        ((_DAT_ffff00009087b5d8 == 0 || (*(int *)(_DAT_ffff00009087b5d8 + 0xc) == 0)))) &&
       (((_DAT_ffff00009087b5e0 == 0 || (*(int *)(_DAT_ffff00009087b5e0 + 0xc) == 0)) &&
        ((_DAT_ffff00009087b5e8 == 0 || (*(int *)(_DAT_ffff00009087b5e8 + 0xc) == 0)))))) {
      _DAT_ffff00009087a9a8 = 0;
      FUN_ffff00009075a6f8();
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0x4c9,
             s_dp_handle____NULL_ffff0000907e81a2);
}


