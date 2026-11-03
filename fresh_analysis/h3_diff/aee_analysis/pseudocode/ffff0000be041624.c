
undefined8 FUN_ffff0000be041624(undefined8 *param_1,int param_2)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  undefined8 uVar4;
  code *pcVar5;
  ulong uVar6;
  
  if (param_1 != (undefined8 *)0x0) {
    puVar3 = (uint *)FUN_ffff0000be0443c4(*(undefined4 *)(param_1 + 0x1c));
    uVar2 = FUN_ffff0000be0440c4(*(undefined4 *)(param_1 + 0x1c));
    puVar1 = PTR_DAT_ffff0000be0f5660;
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
    if ((DAT_ffff0000be124ad8 & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be124ae0,0,0xc50);
      DAT_ffff0000be124ae4 = 0xf;
      DAT_ffff0000be124ad8 = 1;
    }
    if ((((DAT_ffff0000be124ae8 != 0) &&
         ((DAT_ffff0000be125710 == 0 || (*(int *)(DAT_ffff0000be125710 + 0xc) == 0)))) &&
        ((DAT_ffff0000be125718 == 0 || (*(int *)(DAT_ffff0000be125718 + 0xc) == 0)))) &&
       (((DAT_ffff0000be125720 == 0 || (*(int *)(DAT_ffff0000be125720 + 0xc) == 0)) &&
        ((DAT_ffff0000be125728 == 0 || (*(int *)(DAT_ffff0000be125728 + 0xc) == 0)))))) {
      DAT_ffff0000be124ae8 = 0;
      FUN_ffff0000be0456d4();
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x4c9,
             s_dp_handle____NULL_ffff0000be0b463c);
}


