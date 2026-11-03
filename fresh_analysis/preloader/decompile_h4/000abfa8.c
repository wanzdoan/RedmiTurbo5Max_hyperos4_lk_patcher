// File offset entry: 0xabfa8

undefined4 FUN_020abea8(long param_1,byte param_2)

{
  undefined4 uVar1;
  
  if (uRam00000000020e44b0 < param_2) {
    uVar1 = 0x7246;
  }
  else {
    *(int *)((ulong)uRam00000000020e44a4 + param_1) = 0x7f << (ulong)(uRam00000000020e44ac & 0x1f);
    if (param_2 != 0) {
      *(uint *)((ulong)uRam00000000020e44a8 + param_1) =
           (uint)param_2 << (ulong)(uRam00000000020e44ac & 0x1f);
    }
    uVar1 = 0;
    *(int *)((ulong)uRam00000000020e44b4 + param_1) = 1 << (ulong)(uRam00000000020e44b8 & 0x1f);
  }
  return uVar1;
}


