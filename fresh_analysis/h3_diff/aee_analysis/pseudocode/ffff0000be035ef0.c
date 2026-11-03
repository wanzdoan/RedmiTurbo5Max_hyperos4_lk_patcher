
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be035ef0(void)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  undefined8 *puVar4;
  
  if ((DAT_ffff0000be12185c & 1) == 0) {
    if (_DAT_ffff0000be121858 == 0) {
      _DAT_ffff0000be121858 = _DAT_ffff000013260050 & 0xff;
    }
    lVar3 = 0;
    puVar4 = (undefined8 *)0xffff0000be0c98a0;
    do {
      uVar2 = 0xeeeeeeee;
      switch((int)lVar3) {
      case 7:
        uVar2 = _DAT_ffff0000be121858 & 0xff | _DAT_ffff000013260050 & 0xffffff00;
        FUN_ffff0000be05fd98(s_s4__0x_x_ffff0000be0b42b6,uVar2);
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
        uVar2 = _DAT_ffff0000be121858 & 0xff;
        break;
      case 0x31:
        break;
      }
      (&DAT_ffff0000be121860)[lVar3] = uVar2;
      lVar3 = lVar3 + 1;
      puVar4 = puVar4 + 2;
    } while (lVar3 != 0x138);
    iVar1 = FUN_ffff0000be036064();
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98
                (s_devinfo__s__init_error__d__ffff0000be0a3669,s_V1_0_3_ffff0000be0b5528,iVar1);
    }
    DAT_ffff0000be12185c = 1;
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}


