
undefined8 FUN_ffff0000b8038260(undefined8 *param_1,int param_2)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  undefined8 uVar4;
  code *pcVar5;
  ulong uVar6;
  
  if (param_1 != (undefined8 *)0x0) {
    puVar3 = (uint *)FUN_ffff0000b803b000(*(undefined4 *)(param_1 + 0x1c));
    uVar2 = FUN_ffff0000b803ad00(*(undefined4 *)(param_1 + 0x1c));
    puVar1 = PTR_DAT_ffff0000b8108760;
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
    if ((DAT_ffff0000b81282d8 & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81282e0,0,0xc50);
      DAT_ffff0000b81282e4 = 0xf;
      DAT_ffff0000b81282d8 = 1;
    }
    if ((((DAT_ffff0000b81282e8 != 0) &&
         ((DAT_ffff0000b8128f10 == 0 || (*(int *)(DAT_ffff0000b8128f10 + 0xc) == 0)))) &&
        ((DAT_ffff0000b8128f18 == 0 || (*(int *)(DAT_ffff0000b8128f18 + 0xc) == 0)))) &&
       (((DAT_ffff0000b8128f20 == 0 || (*(int *)(DAT_ffff0000b8128f20 + 0xc) == 0)) &&
        ((DAT_ffff0000b8128f28 == 0 || (*(int *)(DAT_ffff0000b8128f28 + 0xc) == 0)))))) {
      DAT_ffff0000b81282e8 = 0;
      FUN_ffff0000b803c310();
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x4c9,
             s_dp_handle____NULL_ffff0000b8092b68);
}


