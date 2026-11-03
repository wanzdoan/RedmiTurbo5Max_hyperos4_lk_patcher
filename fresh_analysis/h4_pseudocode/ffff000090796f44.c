
void FUN_ffff000090796f44(undefined1 *param_1)

{
  ulong uVar1;
  undefined4 *puVar2;
  
  FUN_ffff00009077e11c(s_swpcb_lun__d_ffff0000907ed510,*param_1);
  FUN_ffff00009077e11c(s_swpcb_data_length__d_ffff0000907f582a,param_1[1]);
  if (0xf < (byte)param_1[1]) {
    uVar1 = 0;
    puVar2 = (undefined4 *)(param_1 + 0x1c);
    do {
      FUN_ffff00009077e11c
                (s_swpcb_entry__d__wpt_wpf_0x_x_ffff0000907e05b3,uVar1 & 0xffffffff,
                 *(undefined1 *)(puVar2 + -3));
      FUN_ffff00009077e11c
                (s_swpcb_entry__d__upper_addr_0x_x_ffff0000908019a6,uVar1 & 0xffffffff,puVar2[-2]);
      FUN_ffff00009077e11c
                (s_swpcb_entry__d__lower_addr_0x_x_ffff0000907e05d1,uVar1 & 0xffffffff,puVar2[-1]);
      FUN_ffff00009077e11c
                (s_swpcb_entry__d__block_num_0x_x_ffff0000907dbb15,uVar1 & 0xffffffff,*puVar2);
      uVar1 = uVar1 + 1;
      puVar2 = puVar2 + 4;
    } while (uVar1 < (byte)param_1[1] >> 4);
  }
  return;
}


