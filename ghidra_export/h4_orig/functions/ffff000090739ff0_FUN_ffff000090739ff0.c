// ffff000090739ff0  size=64  FUN_ffff000090739ff0

undefined8 FUN_ffff000090739ff0(long param_1,int param_2)

{
  FUN_ffff000090739b00
            ((uint *)(param_1 + 0x60),
             *(uint *)(param_1 + 0x60) & 0xfffffffe | param_2 != 0 | 0x77000000);
  return 0;
}


