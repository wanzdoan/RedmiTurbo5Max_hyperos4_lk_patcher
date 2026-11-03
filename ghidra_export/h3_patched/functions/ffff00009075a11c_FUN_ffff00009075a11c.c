// ffff00009075a11c  size=256  FUN_ffff00009075a11c

undefined8 FUN_ffff00009075a11c(ulong param_1,uint param_2)

{
  undefined8 uVar1;
  long *plVar2;
  ulong uVar3;
  
  if (0x101 < param_2) {
    return 0;
  }
  if (*(uint *)(&DAT_ffff00009080d5dc + (ulong)param_2 * 0xc) == 0xffffffff) {
    return 0;
  }
  switch(*(int *)(&DAT_ffff00009080d5e0 + (ulong)param_2 * 0xc)) {
  case 0:
    plVar2 = &DAT_ffff00009081e4c8;
    break;
  case 1:
    goto LAB_ffff00009075a1dc;
  case 2:
    plVar2 = &DAT_ffff00009081e4d8;
    break;
  default:
    if ((param_2 & 0xfffffffe) != 0xc2) {
      if (*(int *)(&DAT_ffff00009080d5e0 + (ulong)param_2 * 0xc) == 0x16) {
        plVar2 = &DAT_ffff00009081e4e0;
        break;
      }
      if ((param_2 != 0xc4) && (param_2 != 0xc1)) {
        return 0;
      }
    }
    goto LAB_ffff00009075a1dc;
  case 0x14:
    plVar2 = &DAT_ffff00009081e4d0;
    break;
  case 0x15:
LAB_ffff00009075a1dc:
    uVar1 = FUN_ffff00009075a200();
    return uVar1;
  }
  uVar3 = -(param_1 >> 0x1f & 1) & 0xffffffe000000000 | (param_1 & 0xffffffff) << 5;
  *(uint *)(*plVar2 + 0x34 + uVar3) =
       *(uint *)(*plVar2 + 0x34 + uVar3) |
       1 << (ulong)(*(uint *)(&DAT_ffff00009080d5dc + (ulong)param_2 * 0xc) & 0x1f);
  return 0;
}


