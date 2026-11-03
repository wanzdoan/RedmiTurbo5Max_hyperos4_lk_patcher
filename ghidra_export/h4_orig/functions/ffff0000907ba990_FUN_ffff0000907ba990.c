// ffff0000907ba990  size=420  FUN_ffff0000907ba990

ulong FUN_ffff0000907ba990(long param_1,undefined4 *param_2)

{
  uint *puVar1;
  ulong uVar2;
  int iVar3;
  uint uVar4;
  long lVar5;
  int iVar6;
  
  if (param_2 == (undefined4 *)0x0) {
    return 0x7247;
  }
  puVar1 = (uint *)(param_1 + 4);
  iVar6 = 10;
  *(undefined4 *)(param_1 + 0x20) = 0x11;
  do {
    uVar2 = FUN_ffff0000907bbd64(puVar1,0x10,0,1);
    if ((int)uVar2 == 0) {
      *param_2 = *(undefined4 *)(param_1 + 0x30);
      return uVar2;
    }
    uVar4 = *puVar1;
    if ((uVar4 >> 0x10 & 1) == 0) {
      if ((uVar4 >> 0x18 & 1) != 0) {
        lVar5 = 1;
        goto LAB_ffff0000907baa5c;
      }
      if ((uVar4 >> 0x19 & 1) != 0) {
        lVar5 = 2;
        goto LAB_ffff0000907baa5c;
      }
      if ((uVar4 >> 0x1a & 1) != 0) {
        lVar5 = 3;
        goto LAB_ffff0000907baa5c;
      }
      if ((uVar4 >> 0x1b & 1) != 0) {
        lVar5 = 4;
        goto LAB_ffff0000907baa5c;
      }
      if ((uVar4 >> 0x1c & 1) != 0) {
        lVar5 = 5;
        goto LAB_ffff0000907baa5c;
      }
      iVar3 = 0;
    }
    else {
      lVar5 = 0;
LAB_ffff0000907baa5c:
      iVar3 = (&DAT_ffff0000908136c4)[lVar5 * 2];
    }
    if (iVar3 == 0x72f0) {
      *(undefined4 *)(param_1 + 0x20) = 0x11;
    }
    if (iVar3 != 0) {
      *(undefined4 *)(param_1 + 0x10) = 0;
      *(undefined4 *)(param_1 + 0x10) = 1;
    }
    iVar6 = iVar6 + -1;
  } while (iVar6 != 0);
  uVar4 = *puVar1;
  if ((uVar4 >> 0x10 & 1) == 0) {
    if ((uVar4 >> 0x18 & 1) == 0) {
      if ((uVar4 >> 0x19 & 1) == 0) {
        if ((uVar4 >> 0x1a & 1) == 0) {
          if ((uVar4 >> 0x1b & 1) == 0) {
            if ((uVar4 >> 0x1c & 1) == 0) {
              uVar4 = 0;
              goto LAB_ffff0000907baafc;
            }
            lVar5 = 5;
          }
          else {
            lVar5 = 4;
          }
        }
        else {
          lVar5 = 3;
        }
      }
      else {
        lVar5 = 2;
      }
    }
    else {
      lVar5 = 1;
    }
  }
  else {
    lVar5 = 0;
  }
  uVar4 = (&DAT_ffff0000908136c4)[lVar5 * 2];
LAB_ffff0000907baafc:
  if (uVar4 == 0) {
    uVar2 = 0x7264;
  }
  else {
    uVar2 = (ulong)uVar4;
  }
  return uVar2;
}


