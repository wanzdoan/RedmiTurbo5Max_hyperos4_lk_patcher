
void FUN_ffff000090768ae0(void)

{
  long unaff_x20;
  long unaff_x24;
  long unaff_x25;
  long unaff_x26;
  long unaff_x27;
  long unaff_x28;
  long unaff_x29;
  long in_stack_00000008;
  long in_stack_00000010;
  long in_stack_00000018;
  long in_stack_00000020;

  if ((((unaff_x24 != 0) && (FUN_ffff0000907172e8(), unaff_x25 != 0)) &&
      (FUN_ffff0000907172e8(), unaff_x26 != 0)) &&
     ((FUN_ffff0000907172e8(), unaff_x27 != 0 && (FUN_ffff0000907172e8(), unaff_x28 != 0)))) {
    FUN_ffff0000907172e8();
    if (*(long *)(unaff_x29 + -8) != 0) {
      FUN_ffff0000907172e8(*(undefined8 *)(unaff_x29 + -8));
      if (*(long *)(unaff_x29 + -0x10) != 0) {
        FUN_ffff0000907172e8(*(undefined8 *)(unaff_x29 + -0x10));
        if (*(long *)(unaff_x29 + -0x18) != 0) {
          FUN_ffff0000907172e8(*(undefined8 *)(unaff_x29 + -0x18));
          if (in_stack_00000020 != 0) {
            FUN_ffff0000907172e8(in_stack_00000020);
            if (in_stack_00000018 != 0) {
              FUN_ffff0000907172e8(in_stack_00000018);
              if (in_stack_00000010 != 0) {
                FUN_ffff0000907172e8(in_stack_00000010);
                if ((in_stack_00000008 != 0) &&
                   (FUN_ffff0000907172e8(in_stack_00000008), unaff_x20 != 0)) {
                  FUN_ffff0000907172e8();
                  FUN_ffff000090709cfc(s_Create_thread_success__start_tes_ffff0000907d074e);
                  FUN_ffff000090709dec(&DAT_ffff0000907d788e);
                  return;
                }
              }
            }
          }
        }
      }
    }
  }
  FUN_ffff000090709dac(s_Create_thread_fail__ffff0000907c80db);
  return;
}
