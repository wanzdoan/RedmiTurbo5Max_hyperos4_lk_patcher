// ffff00009079456c  size=156  FUN_ffff00009079456c

undefined8 FUN_ffff00009079456c(short *param_1)

{
  long lVar1;
  undefined8 uVar2;
  
  lVar1 = FUN_ffff000090792e88(0);
  FUN_ffff00009077eb88(param_1,0,0x9a);
  if (*(short *)(lVar1 + 0x88) == 0) {
    uVar2 = 0xffffffff;
  }
  else {
    *param_1 = *(short *)(lVar1 + 0x88);
    if (*(char *)(lVar1 + 0x8f) != '\0') {
      FUN_ffff00009077ef1c(param_1 + 1,(char *)(lVar1 + 0x8f),0x11);
    }
    if ((ulong)*(byte *)(lVar1 + 0x126) != 0) {
      FUN_ffff00009077ef1c
                ((long)param_1 + 0x13,lVar1 + 0xa5,(ulong)*(byte *)(lVar1 + 0x126) << 1 | 1);
    }
    FUN_ffff00009077ef1c(param_1 + 0x4a,lVar1 + 0xa0,5);
    uVar2 = 0;
  }
  return uVar2;
}


