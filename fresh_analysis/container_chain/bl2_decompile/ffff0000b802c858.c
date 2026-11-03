
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000b802c858(void)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  undefined8 *puVar4;
  
  if ((DAT_ffff0000b812505c & 1) == 0) {
    if (_DAT_ffff0000b8125058 == 0) {
      _DAT_ffff0000b8125058 = _DAT_ffff000013260050 & 0xff;
    }
    lVar3 = 0;
    puVar4 = (undefined8 *)0xffff0000b80a40f0;
    do {
      uVar2 = 0xeeeeeeee;
      switch((int)lVar3) {
      case 7:
        uVar2 = _DAT_ffff0000b8125058 & 0xff | _DAT_ffff000013260050 & 0xffffff00;
        FUN_ffff0000b804c33c(s_s4__0x_x_ffff0000b80927e2,uVar2);
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
        uVar2 = _DAT_ffff0000b8125058 & 0xff;
        break;
      case 0x31:
        break;
      }
      (&DAT_ffff0000b8125060)[lVar3] = uVar2;
      lVar3 = lVar3 + 1;
      puVar4 = puVar4 + 2;
    } while (lVar3 != 0x138);
    iVar1 = FUN_ffff0000b802c9cc();
    if (iVar1 != 0) {
      FUN_ffff0000b804c33c
                (s_devinfo__s__init_error__d__ffff0000b8085558,s_V1_0_3_ffff0000b8093755,iVar1);
    }
    DAT_ffff0000b812505c = 1;
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}


