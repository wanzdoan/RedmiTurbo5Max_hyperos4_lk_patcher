// ffff000090735abc  size=184  FUN_ffff000090735abc

undefined8 FUN_ffff000090735abc(long param_1,long *param_2)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  ulong uVar4;
  char *pcVar5;
  
  if (param_1 == 0) {
    uVar2 = 0xffffffea;
  }
  else {
    iVar1 = FUN_ffff0000907356e8();
    if (iVar1 < 0) {
      uVar2 = 0xfffffffb;
    }
    else {
      uVar4 = (ulong)*(uint *)(DAT_ffff0000908192b0 + 0x50);
      if (0 < (int)*(uint *)(DAT_ffff0000908192b0 + 0x50)) {
        pcVar5 = (char *)(DAT_ffff0000908192b8 + 1);
        lVar3 = DAT_ffff0000908192c0;
        do {
          if ((pcVar5[-1] == '\0') && (*pcVar5 == '\0')) break;
          uVar2 = FUN_ffff00009077f080(param_1,lVar3,0x48);
          if ((int)uVar2 == 0) {
            *param_2 = lVar3 + 0x48;
            return uVar2;
          }
          lVar3 = lVar3 + 0x6d;
          pcVar5 = pcVar5 + 0x80;
          uVar4 = uVar4 - 1;
        } while (uVar4 != 0);
      }
      uVar2 = 0;
    }
  }
  return uVar2;
}


