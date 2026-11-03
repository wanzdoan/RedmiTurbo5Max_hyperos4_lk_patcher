// ffff00009078fddc  size=272  FUN_ffff00009078fddc

undefined8 FUN_ffff00009078fddc(long *param_1,undefined4 param_2,undefined4 *param_3,char param_4)

{
  undefined8 uVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  undefined4 local_48;
  undefined4 uStack_44;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_48 = 3;
  if (param_4 == '\0') {
    local_48 = 1;
  }
  lVar2 = *param_1;
  local_40 = 0;
  uStack_38 = 0;
  if ((*(uint *)(lVar2 + 0x30) >> 3 & 1) == 0) {
    uVar1 = 0xffffffff;
  }
  else {
    uVar4 = 0xffffffffffffff9c;
    param_1[10] = (long)&local_48;
    *(undefined4 *)(lVar2 + 0x94) = param_2;
    *(undefined4 *)(lVar2 + 0x98) = 0;
    *(undefined4 *)(lVar2 + 0x9c) = 0;
    *(undefined4 *)(lVar2 + 0x90) = local_48;
    uStack_44 = param_2;
    do {
      lVar2 = *param_1;
      if ((*(uint *)(lVar2 + 0x20) >> 10 & 1) != 0) goto LAB_ffff00009078fe8c;
      FUN_ffff00009077cbd8(100);
      uVar4 = uVar4 + 100;
    } while (uVar4 < 0x7a0bd);
    lVar2 = *param_1;
    if ((*(uint *)(lVar2 + 0x20) >> 10 & 1) == 0) {
      uVar1 = 0xffffffff;
      param_1[10] = 0;
    }
    else {
LAB_ffff00009078fe8c:
      *(undefined4 *)(lVar2 + 0x20) = 0x400;
      lVar3 = param_1[10];
      if (lVar3 != 0) {
        *(uint *)(lVar3 + 8) = *(uint *)(lVar3 + 8) | *(uint *)(lVar2 + 0x98) & 0xff;
        *(undefined4 *)(lVar3 + 0xc) = *(undefined4 *)(lVar2 + 0x9c);
      }
      param_1[10] = 0;
      if (param_3 == (undefined4 *)0x0) {
        uVar1 = 0;
      }
      else {
        uVar1 = 0;
        *param_3 = local_40._4_4_;
      }
    }
  }
  return uVar1;
}


