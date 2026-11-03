
undefined8 FUN_ffff00009079e634(char *param_1,char *param_2,char *param_3)

{
  char cVar1;
  long lVar2;
  undefined8 uVar3;
  uint uVar4;
  ushort *puVar5;
  ushort *puVar6;
  
  if (((param_1 == (char *)0x0) || (param_2 == (char *)0x0)) || (param_3 == (char *)0x0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e064d,0x57c,
               s_a______void__0_____m______void___ffff0000907e57c1);
  }
  if (*param_2 == '\x01') {
    return 0xfffffffd;
  }
  uVar4 = *(uint *)(param_1 + 8);
  if (*(uint *)(param_2 + 8) < uVar4) {
LAB_ffff00009079e684:
    uVar3 = FUN_ffff00009079dafc(param_1,param_2,0,param_3);
    if ((int)uVar3 != 0) {
      return uVar3;
    }
    cVar1 = *param_3;
  }
  else {
    if (*(uint *)(param_2 + 8) <= uVar4) {
      if (-1 < (int)(uVar4 - 1)) {
        lVar2 = (ulong)(uVar4 - 1) * 2;
        puVar5 = (ushort *)(*(long *)(param_2 + 0x10) + lVar2);
        puVar6 = (ushort *)(*(long *)(param_1 + 0x10) + lVar2);
        do {
          if (*puVar5 < *puVar6) goto LAB_ffff00009079e684;
          if (*puVar6 < *puVar5) goto LAB_ffff00009079e6b0;
          puVar6 = puVar6 + -1;
          puVar5 = puVar5 + -1;
          uVar4 = uVar4 - 1;
        } while (0 < (int)uVar4);
      }
      FUN_ffff00009077ee50(*(undefined8 *)(param_3 + 0x10),0,(ulong)*(uint *)(param_3 + 4) << 1);
      *param_3 = '\0';
      param_3[8] = '\x01';
      param_3[9] = '\0';
      param_3[10] = '\0';
      param_3[0xb] = '\0';
      return 0;
    }
LAB_ffff00009079e6b0:
    uVar3 = FUN_ffff00009079c1d4(param_1,param_3);
    if ((int)uVar3 != 0) {
      return uVar3;
    }
    cVar1 = *param_1;
  }
  if ((cVar1 == '\x01') && (uVar3 = FUN_ffff00009079ce30(param_3,param_2,param_3), (int)uVar3 != 0))
  {
    return uVar3;
  }
  return 0;
}


