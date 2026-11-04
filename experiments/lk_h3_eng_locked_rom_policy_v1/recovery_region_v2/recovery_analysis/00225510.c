
/* std::string::__throw_length_error[abi:ne180000]() const */

void std::string::__throw_length_error_abi_ne180000_(void)

{
  code *pcVar1;

  std::__throw_length_error_abi_ne180000_("basic_string");
                    /* WARNING: Does not return */
  pcVar1 = (code *)UndefinedInstructionException(0,0x225524);
  (*pcVar1)();
}
