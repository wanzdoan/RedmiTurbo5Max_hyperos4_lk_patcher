// ffff000090728088  size=40  FUN_ffff000090728088

void FUN_ffff000090728088(long *param_1,ulong param_2,uint param_3)

{
  *(uint *)(*param_1 + (ulong)*(ushort *)(PTR_DAT_ffff000090839968 + (param_2 & 0xff) * 2)) =
       param_3 & 0xffff;
  return;
}


