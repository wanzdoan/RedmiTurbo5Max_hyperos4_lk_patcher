// ffff00009074ac78  size=372  FUN_ffff00009074ac78

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009074ac78(void)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  undefined8 *puVar4;
  
  if ((DAT_ffff000090877718 & 1) == 0) {
    if (_DAT_ffff000090877714 == 0) {
      _DAT_ffff000090877714 = _DAT_ffff000013260050 & 0xff;
    }
    lVar3 = 0;
    puVar4 = (undefined8 *)0xffff00009080b968;
    do {
      uVar2 = 0xeeeeeeee;
      switch((int)lVar3) {
      case 7:
        uVar2 = _DAT_ffff000090877714 & 0xff | _DAT_ffff000013260050 & 0xffffff00;
        FUN_ffff00009077e11c(s_s4__0x_x_ffff0000907e7e1c,uVar2);
        break;
      default:
        uVar2 = *(uint *)*puVar4;
        break;
      case 10:
        uVar2 = 0x56ab0004;
        break;
      case 0x1d:
        uVar2 = _DAT_ffff0000132607cc;
        if (_DAT_ffff0000132607a0 >> 0x1e == 0) {
          uVar2 = _DAT_ffff0000132607a0;
        }
        break;
      case 0x1e:
        uVar2 = _DAT_ffff000090877714 & 0xff;
        break;
      case 0x31:
        break;
      }
      *(uint *)(lVar3 * 4 + -0xffff6f7888e4) = uVar2;
      lVar3 = lVar3 + 1;
      puVar4 = puVar4 + 2;
    } while (lVar3 != 0x138);
    iVar1 = FUN_ffff00009074adec();
    if (iVar1 != 0) {
      FUN_ffff00009077e11c
                (s_devinfo__s__init_error__d__ffff0000907cfaa5,s_V1_0_3_ffff0000907e9850,iVar1);
    }
    DAT_ffff000090877718 = 1;
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}


