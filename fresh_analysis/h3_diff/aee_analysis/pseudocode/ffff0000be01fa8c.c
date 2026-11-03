
char * FUN_ffff0000be01fa8c(char *param_1)

{
  char cVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  long lVar6;
  long lVar7;
  char *pcVar8;
  
  if ((DAT_ffff0000be1176a0 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_common_dconfig_ffff0000be0c3eb5,0xec,
               s_g_env_init_done____DCONFIG_VALID_ffff0000be0c0675);
  }
  if ((DAT_ffff0000be1176a4 == '\x01') &&
     (FUN_ffff0000be05fd98
                (s__s__d__get_env__s_ffff0000be0b6342,s_dconfig_getenv_ffff0000be0b2d92,0xf0,param_1
                ), *DAT_ffff0000be1176b8 != '\0')) {
    lVar4 = 0;
    uVar2 = 0;
    do {
      lVar7 = (long)(int)uVar2;
      uVar3 = uVar2;
      if ((int)uVar2 < 0x401) {
        uVar3 = 0x400;
      }
      lVar6 = (ulong)uVar3 - lVar7;
      uVar2 = uVar2 + 1;
      pcVar8 = DAT_ffff0000be1176b8 + lVar7 + 1;
      do {
        if (lVar6 == 0) {
          return (char *)0x0;
        }
        cVar1 = *pcVar8;
        lVar6 = lVar6 + -1;
        uVar2 = uVar2 + 1;
        pcVar8 = pcVar8 + 1;
      } while (cVar1 != '\0');
      pcVar8 = param_1;
      if (param_1 != (char *)0x0) {
        do {
          cVar1 = *pcVar8;
          iVar5 = (int)lVar4;
          if (cVar1 != DAT_ffff0000be1176b8[lVar4]) {
            if (((cVar1 != '\0') || (DAT_ffff0000be1176b8[lVar4] != '=')) || (iVar5 + 1 < 0))
            goto LAB_ffff0000be01fb70;
            goto LAB_ffff0000be01fb98;
          }
          lVar4 = lVar4 + 1;
          pcVar8 = pcVar8 + 1;
        } while (cVar1 != '=');
        if (-1 < (int)lVar4) {
LAB_ffff0000be01fb98:
          return DAT_ffff0000be1176b8 + (iVar5 + 1);
        }
      }
LAB_ffff0000be01fb70:
      lVar4 = (long)(int)uVar2;
    } while (DAT_ffff0000be1176b8[lVar4] != '\0');
  }
  return (char *)0x0;
}


