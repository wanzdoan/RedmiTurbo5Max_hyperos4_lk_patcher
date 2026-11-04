
undefined8 FUN_ffff0000907c5888(undefined8 param_1,undefined4 *param_2)

{
  uint uVar1;
  undefined8 uVar2;
  undefined1 *in_x5;
  ulong uVar3;

  uVar2 = 2;
  if ((param_2 != (undefined4 *)0x0) && (in_x5 != (undefined1 *)0x0)) {
    uVar1 = FUN_ffff0000907c1030(*param_2);
    *in_x5 = 0;
    if ((uVar1 & 0xfffffff8) == 0x800) {
      uVar2 = FUN_ffff00009077f450(&DAT_ffff000090830728,param_2 + 2,0x100);
      if ((int)uVar2 == 0) {
        *in_x5 = 1;
        return uVar2;
      }
      FUN_ffff00009077ea84(s__AVB__vbmeta_avb_key___ffff0000907eb09a);
      uVar3 = 0;
      do {
        FUN_ffff00009077ea84(s_0x_x_ffff0000907f746e,*(undefined1 *)((long)(param_2 + 2) + uVar3));
        uVar3 = uVar3 + 1;
        if ((uVar3 & 0xf) == 0) {
          FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
        }
      } while (uVar3 != 0x100);
    }
    else {
      FUN_ffff00009077ea84(s__AVB__invalid_pubk_size_ffff0000907eb081);
    }
    uVar2 = 0;
  }
  return uVar2;
}
