// ffff000090793e28  size=228  FUN_ffff000090793e28

undefined8
FUN_ffff000090793e28
          (long param_1,int param_2,undefined1 param_3,undefined1 param_4,undefined1 param_5,
          uint *param_6)

{
  uint uVar1;
  undefined8 uVar2;
  
  if (param_6 == (uint *)0x0) {
LAB_ffff000090793ec0:
    uVar2 = 0xffffffff;
  }
  else {
    FUN_ffff00009077eb88((undefined1 *)(param_1 + 0x1c0),0,0x18);
    FUN_ffff00009077eb88(param_1 + 0x1e0,0,0x18);
    *(char *)(param_1 + 0x1c4) = (char)param_2;
    *(undefined1 *)(param_1 + 0x1c5) = param_3;
    *(undefined1 *)(param_1 + 0x1c6) = param_4;
    *(undefined1 *)(param_1 + 0x1c7) = param_5;
    if (param_2 == 3) {
      *(undefined1 *)(param_1 + 0x1c0) = 1;
    }
    else {
      if (param_2 != 4) goto LAB_ffff000090793ec0;
      uVar1 = (*param_6 & 0xff00ff00) >> 8 | (*param_6 & 0xff00ff) << 8;
      *(undefined1 *)(param_1 + 0x1c0) = 0x81;
      *(uint *)(param_1 + 0x1cc) = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    uVar2 = FUN_ffff000090793b8c(param_1,1,5000);
    if ((int)uVar2 == 0) {
      uVar1 = (*(uint *)(param_1 + 0x1ec) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x1ec) & 0xff00ff) << 8;
      *param_6 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
  }
  return uVar2;
}


