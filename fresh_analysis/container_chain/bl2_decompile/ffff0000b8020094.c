
ulong FUN_ffff0000b8020094(int param_1)

{
  ulong uVar1;
  long lVar2;
  ushort local_1c [2];
  ushort local_18 [2];
  ushort local_14 [2];
  
  if (DAT_ffff0000b811b680 == (undefined8 *)0x0) {
    uVar1 = 0xffffffda;
  }
  else if (param_1 == 2) {
    lVar2 = DAT_ffff0000b811b680[2];
    local_14[0] = 0;
    (*(code *)*DAT_ffff0000b811b680)
              (*(undefined2 *)(lVar2 + 0x1e),local_14,*(undefined2 *)(lVar2 + 0x20),
               *(undefined2 *)(lVar2 + 0x22));
    uVar1 = (ulong)local_14[0];
  }
  else if (param_1 == 1) {
    lVar2 = DAT_ffff0000b811b680[2];
    local_18[0] = 0;
    (*(code *)*DAT_ffff0000b811b680)
              (*(undefined2 *)(lVar2 + 0x18),local_18,*(undefined2 *)(lVar2 + 0x1a),
               *(undefined2 *)(lVar2 + 0x1c));
    uVar1 = (ulong)local_18[0];
  }
  else if (param_1 == 0) {
    lVar2 = DAT_ffff0000b811b680[2];
    local_1c[0] = 0;
    (*(code *)*DAT_ffff0000b811b680)
              (*(undefined2 *)(lVar2 + 0x12),local_1c,*(undefined2 *)(lVar2 + 0x14),
               *(undefined2 *)(lVar2 + 0x16));
    uVar1 = (ulong)local_1c[0];
  }
  else {
    uVar1 = 0xfffffff8;
  }
  return uVar1;
}


