
undefined8 FUN_ffff0000907563bc(undefined8 *param_1,int param_2)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  undefined8 uVar4;
  code *pcVar5;
  ulong uVar6;
  
  if (param_1 != (undefined8 *)0x0) {
    puVar3 = (uint *)FUN_ffff0000907593e8(*(undefined4 *)(param_1 + 0x1c));
    uVar2 = FUN_ffff0000907590e8(*(undefined4 *)(param_1 + 0x1c));
    puVar1 = PTR_DAT_ffff0000908399c8;
    if (0 < (int)uVar2) {
      uVar6 = (ulong)uVar2;
      do {
        if ((*(long *)(puVar1 + (ulong)*puVar3 * 8) != 0) &&
           (pcVar5 = *(code **)(*(long *)(puVar1 + (ulong)*puVar3 * 8) + 0x58),
           pcVar5 != (code *)0x0)) {
          if (param_2 == 0) {
            uVar4 = 0;
          }
          else {
            uVar4 = *param_1;
          }
          (*pcVar5)((ulong)*puVar3,uVar4);
        }
        uVar6 = uVar6 - 1;
        puVar3 = puVar3 + 1;
      } while (uVar6 != 0);
    }
    *(undefined4 *)((long)param_1 + 0xc) = 0;
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
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0x4c9,
             s_dp_handle____NULL_ffff0000907e81a2);
}


