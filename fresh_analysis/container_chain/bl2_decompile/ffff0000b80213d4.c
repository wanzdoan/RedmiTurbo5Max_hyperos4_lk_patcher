
void FUN_ffff0000b80213d4(undefined8 param_1,ulong param_2)

{
  FUN_ffff0000b804cd40
            (&DAT_ffff0000b811b828,param_1,
             -(param_2 >> 0x1f & 1) & 0xfffffffe00000000 | (param_2 & 0xffffffff) << 1);
  return;
}


