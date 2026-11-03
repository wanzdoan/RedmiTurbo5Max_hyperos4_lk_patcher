// ffff00009079f208  size=208  FUN_ffff00009079f208

undefined8 FUN_ffff00009079f208(long param_1,ushort *param_2)

{
  ushort uVar1;
  ushort *puVar2;
  uint uVar3;
  ushort *puVar4;
  ulong uVar5;
  ushort *puVar6;
  
  if ((param_1 != 0) && (param_2 != (ushort *)0x0)) {
    puVar4 = *(ushort **)(param_1 + 0x10);
    puVar6 = puVar4 + ((ulong)*(uint *)(param_1 + 8) - 1);
    puVar2 = param_2;
    if ((puVar4 == puVar6) && (*puVar4 == 0)) {
      *(undefined1 *)param_2 = 0;
    }
    else {
      for (; puVar4 < puVar6; puVar4 = puVar4 + 1) {
        *puVar2 = *puVar4;
        puVar2 = puVar2 + 1;
      }
      puVar4 = puVar2;
      uVar5 = (ulong)*puVar6;
      if (*puVar6 != 0) {
        do {
          uVar3 = (uint)uVar5;
          puVar2 = (ushort *)((long)puVar4 + 1);
          *(char *)puVar4 = (char)uVar5;
          puVar4 = puVar2;
          uVar5 = uVar5 >> 8;
        } while (0xff < uVar3);
      }
      puVar2 = (ushort *)((long)puVar2 + -1);
      if (param_2 < puVar2) {
        do {
          puVar6 = (ushort *)((long)param_2 + 1);
          uVar1 = *param_2;
          *(char *)param_2 = (char)*puVar2;
          puVar4 = (ushort *)((long)puVar2 + -1);
          *(char *)puVar2 = (char)uVar1;
          puVar2 = puVar4;
          param_2 = puVar6;
        } while (puVar6 < puVar4);
      }
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_sboot_c_ffff0000907e0593,0x997,
             s_mp______void__0_____str______voi_ffff000090803168);
}


